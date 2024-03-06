package ru.vsu.csf.classifiedsweb.services;

import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.Reaction;
import ru.vsu.csf.classifiedsweb.models.User;

import java.security.Principal;

public interface ReactionService {
    Reaction findById(Long id);
    Iterable<Reaction> findAll();
    void create(Principal principal, Advertisement advertisement);
    void deleteById(Long id);
}
