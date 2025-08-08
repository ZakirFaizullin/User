package org.example.service;

import org.example.dao.DAO;
import org.example.dao.DAOImpl;
import org.example.model.User;

import java.util.List;

public class Service {
    private static final DAO dao = new DAOImpl();

    public boolean saveUser(User user) {
        return dao.save(user);
    }

    public User findUser(Long id) {
        return dao.findById(id);
    }

    public List<User> findAllUsers() {
        return dao.findAll();
    }

    public boolean updateUser(User user) {
        return dao.update(user);
    }

    public boolean deleteUser(Long userId) {
        return dao.delete(userId);
    }
}
