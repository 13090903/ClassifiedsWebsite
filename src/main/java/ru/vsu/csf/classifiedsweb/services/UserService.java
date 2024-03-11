package ru.vsu.csf.classifiedsweb.services;

import ru.vsu.csf.classifiedsweb.models.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    boolean createUser(User user);

    List<User> findAll();

    List<User> findAllSortedByRating();

    void banUser(Long id);

    User getUserByPrincipal(Principal principal);

    void addRatingById(Long id, Long value);
}
