package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {} // приватный конструктор для блокировки создания экземпляра класса

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(User.class);
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
