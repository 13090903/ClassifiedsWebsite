package ru.vsu.csf.classifiedsweb.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.Reaction;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.repositories.ReactionRepository;
import ru.vsu.csf.classifiedsweb.repositories.UserRepository;
import ru.vsu.csf.classifiedsweb.services.ReactionService;
import ru.vsu.csf.classifiedsweb.util.exceptions.AdvertisementNotFoundException;
import ru.vsu.csf.classifiedsweb.util.exceptions.ReactionNotFoundException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    @Autowired
    private final ReactionRepository reactionRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public Reaction findById(Long id) {
        Optional<Reaction> reaction = reactionRepository.findById(id);
        return reaction.orElseThrow(ReactionNotFoundException::new);
    }

    @Override
    public Iterable<Reaction> findAll() {
        return reactionRepository.findAll();
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());
    }

    @Override
    public void create(Principal principal, Advertisement advertisement) {
        Reaction reaction = new Reaction();
        reaction.setUser(getUserByPrincipal(principal));
        reaction.setAdvertisement(advertisement);
        reactionRepository.save(reaction);
    }

    @Override
    public List<User> findUsersByAdvertisementId(Long id) {
        List<Reaction> reactions = reactionRepository.findAll();
        List<User> users = new ArrayList<>();
        for (Reaction reaction : reactions) {
            if (reaction.getAdvertisement().getId().equals(id)) {
                users.add(reaction.getUser());
            }
        }
        return users;
    }

    @Override
    public List<Advertisement> findAdvertisementsByUserId(Long id) {
        List<Reaction> reactions = reactionRepository.findAll();
        List<Advertisement> advertisements = new ArrayList<>();
        for (Reaction reaction : reactions) {
            if (reaction.getUser().getId().equals(id)) {
                advertisements.add(reaction.getAdvertisement());
            }
        }
        return advertisements;
    }

    @Override
    public Reaction findByUserAdvertisement(User user, Advertisement advertisement) {
        List<Reaction> reactions = reactionRepository.findAll();
        for (Reaction reaction : reactions) {
            if (reaction.getUser().equals(user) && reaction.getAdvertisement().equals(advertisement)) {
                return reaction;
            }
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Reaction reaction = findById(id);
        log.info("Deleting reaction with id {}", reaction.getId());
        reactionRepository.delete(reaction);
    }
}
