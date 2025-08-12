package org.example.dao;

import org.example.model.User;
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
    void testSaveAndFind() {
        User user = new User("Zakir", 35, "zakir@example.com");
        dao.save(user);

        User savedUser = dao.findById(user.getId());
        assertNotNull(savedUser);
        assertEquals("Zakir", savedUser.getName());
        assertEquals(35, savedUser.getAge());
        assertEquals("zakir@example.com", savedUser.getEmail());
    }

    @Test
    void testUpdate() {
        User user = new User("Zakir", 35, "zakir@example.com");
        dao.save(user);

        User savedUser = dao.findById(user.getId());
        savedUser.setName("John");
        savedUser.setAge(25);
        savedUser.setEmail("john@example.com");
        dao.update(savedUser);

        User updatedUser = dao.findById(user.getId());
        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getName());
        assertEquals(25, updatedUser.getAge());
        assertEquals("john@example.com", updatedUser.getEmail());
    }

    @Test
    void findAll() {
        User user1 = new User("Zakir", 35, "zakir@example.com");
        User user2 = new User("John", 25, "john@example.com");
        dao.save(user1);
        dao.save(user2);

        List<User> users = dao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void delete() {
        List<User> list1 = dao.findAll();
        assertEquals(0, list1.size());
        User user = new User("Zakir", 35, "zakir@example.com");
        dao.save(user);

        List<User> list2 = dao.findAll();
        assertEquals(1, list2.size());

        dao.delete(user.getId());
        List<User> list3 = dao.findAll();
        assertEquals(0, list3.size());
    }

    @Test
    void testFindNonExistentUser() {
        User user = dao.findById(10000L);
        assertNull(user);
    }
}
