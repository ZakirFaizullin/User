package org.example;

import java.util.List;

public interface DAO {
    public void save(User user);

    public User findById(Long id);

    public List<User> findAll();

    public void update(User user);

    public void delete(Long id);
}
