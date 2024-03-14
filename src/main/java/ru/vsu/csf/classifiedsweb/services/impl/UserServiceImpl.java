package ru.vsu.csf.classifiedsweb.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.csf.classifiedsweb.enums.Role;
import ru.vsu.csf.classifiedsweb.models.Image;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.repositories.UserRepository;
import ru.vsu.csf.classifiedsweb.services.UserService;

import java.io.IOException;
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
    public void update(Long id, User user,  MultipartFile file1) throws IOException {
        User oldUser = userRepository.findById(id).orElse(null);
        if (oldUser != null) {
            oldUser.setName(user.getName());
            oldUser.setPhoneNumber(user.getPhoneNumber());
            oldUser.setCity(user.getCity());
            addAvatar(oldUser, file1);
            userRepository.save(oldUser);
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

    @Override
    public void addAvatar(User user, MultipartFile file1) throws IOException {
        User user1 = userRepository.findById(user.getId()).orElse(null);
        Image image1;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            if (user1 != null) {
                user1.setAvatar(image1);
                userRepository.save(user1);
            }
        }
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public List<User> findAllSortedByRating() {
        return userRepository.findAllByOrderByRatingDesc();
    }
}