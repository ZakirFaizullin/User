package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.dao.DAO;
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
}
