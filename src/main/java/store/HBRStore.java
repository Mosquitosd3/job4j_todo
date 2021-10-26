package store;

import model.Item;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            update(item.getId(), item);
        }
    }

    private Item create(Item item) {
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error("Error where create", e);
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public void update(Integer id, Item item) {
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            session.createQuery("update Item set"
                            + " description = :description, created = :created, done = :done"
                            + " where id = :id")
                    .setParameter("description", item.getDescription())
                    .setParameter("created", item.getCreated())
                    .setParameter("done", item.isDone())
                    .setParameter("id", item.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error("Error where update", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Integer id) {
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            Item item = new Item(null);
            item.setId(id);
            session.delete(item);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            LOG.error("Error where delete", e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Item> findAll() {
        List result = null;
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            result = session.createQuery("from model.Item").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            LOG.error("Error where find all", e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Item findByID(Integer id) {
        Item result = null;
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            result = session.get(Item.class, id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            LOG.error("Error where find by id", e);
        } finally {
            session.close();
        }
        return result;
    }
}