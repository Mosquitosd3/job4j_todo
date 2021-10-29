package store;

import model.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void save(Item item) {
        if (item.getId() == 0) {
            create(item);
        } else {
            update(item.getId());
        }
    }

    private Item create(Item item) {
        this.tx(session -> session.save(item));
        return item;
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
        return this.tx(session -> session.createQuery("from model.Item").list());
    }

    @Override
    public Item findByID(Integer id) {
        return this.tx(session -> session.get(Item.class, id));
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