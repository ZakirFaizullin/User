package org.example.dao;

import org.example.model.User;

import java.util.List;

public interface DAO {
    boolean save(User user);

    User findById(Long id);

    List<User> findAll();

    boolean update(User user);

    boolean delete(Long id);
}
