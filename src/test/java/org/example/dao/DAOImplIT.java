package org.example.dao;

import org.example.model.User;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@Testcontainers
class DAOImplIT {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:13")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");



    static private DAO dao;

    @BeforeAll
    static void setUp() {
        System.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        System.setProperty("hibernate.connection.password", postgresContainer.getPassword());

        dao = new DAOImpl();
    }

    @AfterEach
    void tearDown() {
        dao.findAll()
                .stream()
                .map(User::getId)
                .forEach(dao::delete);
    }

    @Test
    void testSave() {
        User user = new User("Zakir", 35, "zakir@example.com");
        dao.save(user);

        User savedUser = findInTestDb(user.getId());
        assertNotNull(savedUser);
        assertEquals("Zakir", savedUser.getName());
        assertEquals(35, savedUser.getAge());
        assertEquals("zakir@example.com", savedUser.getEmail());
    }

    @Test
    void testFind() {
        User user = new User("Zakir", 35, "zakir@example.com");
        saveInTestDb(user);

        User savedUser = dao.findById(user.getId());
        assertNotNull(savedUser);
        assertEquals("Zakir", savedUser.getName());
        assertEquals(35, savedUser.getAge());
        assertEquals("zakir@example.com", savedUser.getEmail());
    }

    @Test
    void testUpdate() {
        User user = new User("Zakir", 35, "zakir@example.com");
        saveInTestDb(user);

        User savedUser = findInTestDb(user.getId());
        savedUser.setName("John");
        savedUser.setAge(25);
        savedUser.setEmail("john@example.com");
        dao.update(savedUser);

        User updatedUser = findInTestDb(user.getId());
        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getName());
        assertEquals(25, updatedUser.getAge());
        assertEquals("john@example.com", updatedUser.getEmail());
    }

    @Test
    void testFindAll() {
        User user1 = new User("Zakir", 35, "zakir@example.com");
        User user2 = new User("John", 25, "john@example.com");
        saveInTestDb(user1);
        saveInTestDb(user2);

        List<User> users = dao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void testDelete() {
        List<User> list1 = findAllInTestDb();
        assertEquals(0, list1.size());
        User user = new User("Zakir", 35, "zakir@example.com");
        saveInTestDb(user);

        List<User> list2 = findAllInTestDb();
        assertEquals(1, list2.size());

        dao.delete(user.getId());
        List<User> list3 = findAllInTestDb();
        assertEquals(0, list3.size());
    }

    @Test
    void testFindNonExistentUser() {
        User user = dao.findById(10000L);
        assertNull(user);
    }


    //Ниже вспомогательные методы для работы с тестовой бд

    void saveInTestDb(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    User findInTestDb(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(User.class, id);
        }
    }

    List<User> findAllInTestDb() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

}
