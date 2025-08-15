package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.dao.DAO;
import org.example.exceptions.DbException;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class ServiceTest {

    DAO dao;
    Service service;

    @BeforeEach
    public void beforeEach() {
        dao = Mockito.mock(DAO.class);
        service = new Service(dao);
    }

    @Test
    public void givenTask_whenSaveUser_thenSaveInDb() {
        User user = new User("Name", 1, "email");

        service.saveUser(user);

        Mockito.verify(dao).save(user);
    }

    @Test
    public void givenUsers_whenFindUser_thenReturnUser() {
        User user = new User("Name", 1, "email");
        Mockito.when(service.findUser(1L)).thenReturn(user);

        User actual = service.findUser(1L);

        Assertions.assertThat(actual).isEqualTo(user);
    }

    @Test
    public void givenUsers_whenFindAllUsers_thenReturnListUsers() {
        List<User> users = List.of(new User("Name", 1, "email"),
                new User("Name2", 2, "email2"));
        Mockito.when(service.findAllUsers()).thenReturn(users);

        List<User> actual = service.findAllUsers();

        Assertions.assertThat(actual).isEqualTo(users);
    }

    @Test
    public void givenTask_whenUpdateUser_thenUpdateInDb() {
        User user = new User("Name", 1, "email");

        service.updateUser(user);

        Mockito.verify(dao).update(user);
    }

    @Test
    public void givenId_whenDeleteUser_thenDeleteFromDb() {
        Long id = 1L;

        service.deleteUser(id);

        Mockito.verify(dao).delete(id);
    }

    @Test
    public void dbException_MustContainValidMessage() {
        Exception exception = new RuntimeException("test");
        DbException dbException = new DbException("DbException message", exception);

        Assertions.assertThat("DbException message").isEqualTo(dbException.getMessage());
        Assertions.assertThat(exception).isEqualTo(dbException.getCause());
    }

    @Test
    public void givenId_whenFindById_thenThrowsException() {
        Long id = 1000001L;
        RuntimeException rte = new RuntimeException("manually thrown exception in findById method");
        Mockito.when(dao.findById(id)).thenThrow(new DbException("Error while fetching: " + rte.getMessage(), rte));

        Assertions.assertThatThrownBy(() -> dao.findById(id))
                .isExactlyInstanceOf(DbException.class)
                .hasMessage("Error while fetching: manually thrown exception in findById method");
    }

}
