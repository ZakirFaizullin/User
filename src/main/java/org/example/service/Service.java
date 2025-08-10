package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.DAO;
import org.example.model.User;

import java.util.List;

@AllArgsConstructor
public class Service {
    private final DAO dao;

    public void saveUser(User user) {
        dao.save(user);
    }

    public User findUser(Long id) {
        return dao.findById(id);
    }

    public List<User> findAllUsers() {
        return dao.findAll();
    }

    public void updateUser(User user) {
        dao.update(user);
    }

    public void deleteUser(Long userId) {
        dao.delete(userId);
    }
}
