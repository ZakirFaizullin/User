package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.DBException;
import org.example.utils.HibernateUtil;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DAOImpl implements DAO {
    private static final Logger logger = LogManager.getLogger(DAOImpl.class);

    @Override
    public boolean save(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error while saving", e);
            throw new DBException("Error while saving: " + e.getMessage(), e);
        }
    }

    @Override
    public User findById(Long id) {
        User user = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.find(User.class, id);
        } catch (Exception e) {
            logger.error("Error while fetching", e);
            throw new DBException("Error while fetching: " + e.getMessage(), e);
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
            logger.error("Error when selecting all users", e);
            throw new DBException("Error when selecting all users: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (user != null) {
                transaction = session.beginTransaction();
                session.merge(user);
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found!");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error while updating", e);
            throw new  DBException("Error while updating: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long userId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.find(User.class, userId);
            if (user != null) {
                transaction = session.beginTransaction();
                session.remove(user);
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found!");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error while deleting", e);
            throw new   DBException("Error while deleting: " + e.getMessage(), e);
        }
    }
}
