package ru.vsu.csf.classifiedsweb.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.repositories.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void addRating() {
        User user = createUser();
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.ofNullable(user));
        userService.addRatingById(0L, 15L);
        long result = 138L;
        assert user != null;
        Assertions.assertEquals(user.getRating(), result);
    }

    public User createUser() {
        User user = new User();
        user.setName("a");
        user.setCity("Vor");
        user.setEmail("rrr@mail.ru");
        user.setRating(123L);
        return user;
    }
}
