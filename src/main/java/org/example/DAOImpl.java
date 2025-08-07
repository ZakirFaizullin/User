package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DAOImpl implements DAO {
    private static final Logger logger = LogManager.getLogger(DAOImpl.class);

    @Override
    public void save(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error(e);
            throw e;

        }
    }

    @Override
    public User findById(Long id) {
        User user = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.find(User.class, id);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> list = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            list = session.createQuery("from User", User.class).list();
            return list;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (user != null) {
                transaction = session.beginTransaction();
                session.merge(user);
                transaction.commit();
            } else {
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void delete(Long userId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.find(User.class, userId);
            if (user != null) {
                transaction = session.beginTransaction();
                session.remove(user);
                transaction.commit();
            } else {
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error(e);
            throw e;
        }
    }
}
