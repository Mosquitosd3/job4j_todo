package store;

import model.Category;
import model.Item;
import model.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Query;
import java.util.List;
import java.util.function.Function;

public class HBRStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(HBRStore.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HBRStore() {
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    private static final class Lazy {
        private static final Store INST = new HBRStore();
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == 0) {
            create(item);
        } else {
            update(item.getId());
        }
        return item;
    }

    @Override
    public Users save(Users user) {
        return create(user);
    }

    private Item create(Item item) {
        this.tx(session -> session.save(item));
        return item;
    }

    private Users create(Users user) {
        this.tx(session -> session.save(user));
        return user;
    }

    @Override
    public void update(Integer id) {
        this.tx(session -> session.createQuery(
                "update Item set done = :done where id = " + id)
                .setParameter("done", true).executeUpdate()
        );
    }

    @Override
    public void delete(Integer id) {
        this.tx(session -> {
           Item item = session.load(Item.class, id);
           session.delete(item);
           return item;
        });
    }

    @Override
    public List<Item> findAll() {
        return this.tx(session -> session.createQuery(
                "select distinct i from Item i join fetch i.categories").list()
        );
    }

    @Override
    public List<Category> findAllCategory() {
        return this.tx(session -> session.createQuery("from model.Category").list());
    }

    @Override
    public Item findByID(Integer id) {
        return this.tx(session -> session.get(Item.class, id));
    }

    @Override
    public Users findByEmail(String email) {
        return (Users) this.tx(session -> session.createQuery("from Users where email = :email")
                .setParameter("email", email)
                .uniqueResult()
        );
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }

}