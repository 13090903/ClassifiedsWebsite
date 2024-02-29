package ru.vsu.csf.classifiedsweb.services;

import ru.vsu.csf.classifiedsweb.models.User;

import java.util.List;

public interface UserService {
    boolean createUser(User user);

    List<User> findAll();

    void banUser(Long id);
}
