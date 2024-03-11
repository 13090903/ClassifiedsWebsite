package ru.vsu.csf.classifiedsweb.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vsu.csf.classifiedsweb.enums.Role;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.repositories.UserRepository;
import ru.vsu.csf.classifiedsweb.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {
        String userEmail = user.getEmail();
        if (userRepository.findByEmail(userEmail) != null) return false;
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        user.setRating(0L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving new User with email: {}", userEmail);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user {}", user.getName());
            } else {
                user.setActive(true);
                log.info("Unban user {}", user.getName());
            }
            userRepository.save(user);
        }
    }

    @Override
    public void changeRole(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Set<Role> roleSet = user.getRoles();
            if (roleSet.contains(Role.ROLE_USER)) {
                roleSet.remove(Role.ROLE_USER);
                roleSet.add(Role.ROLE_ADMIN);
            } else if (roleSet.contains(Role.ROLE_ADMIN)){
                roleSet.remove(Role.ROLE_ADMIN);
                roleSet.add(Role.ROLE_USER);
            }
            user.setRoles(roleSet);
            userRepository.save(user);
        }
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    @Override
    public void addRatingById(Long id, Long value) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setRating(user.getRating() + value);
            userRepository.save(user);
        }
    }

    public List<User> findAllSortedByRating() {
        return userRepository.findAllByOrderByRatingDesc();
    }
}