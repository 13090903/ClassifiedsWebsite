package ru.vsu.csf.classifiedsweb.services;

import org.springframework.web.multipart.MultipartFile;
import ru.vsu.csf.classifiedsweb.models.User;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    boolean createUser(User user);

    List<User> findAll();

    List<User> findAllSortedByRating();

    void banUser(Long id);
    void changeRole(Long id);

    void update(Long id, User user,  MultipartFile file1) throws IOException;

    User getUserByPrincipal(Principal principal);

    void addRatingById(Long id, Long value);

    void addAvatar(User user, MultipartFile file1) throws IOException;
}
