package ru.vsu.csf.classifiedsweb.services;

import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.Reaction;
import ru.vsu.csf.classifiedsweb.models.User;

import java.security.Principal;
import java.util.List;

public interface ReactionService {
    Reaction findById(Long id);
    Iterable<Reaction> findAll();
    void create(Principal principal, Advertisement advertisement);
    List<User> findUsersByAdvertisementId(Long id);
    List<Advertisement> findAdvertisementsByUserId(Long id);
    Reaction findByUserAdvertisement(User user, Advertisement advertisement);
    void deleteById(Long id);

    User getUserByPrincipal(Principal principal);
}
