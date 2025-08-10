package org.example.dao;

import org.example.model.User;

import java.util.List;

public interface DAO {
    void save(User user);

    User findById(Long id);

    List<User> findAll();

    void update(User user);

    void delete(Long id);
}
