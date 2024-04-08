package ru.vsu.csf.classifiedsweb.services.impl;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.Reaction;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.repositories.ReactionRepository;
import ru.vsu.csf.classifiedsweb.repositories.UserRepository;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReactionServiceImplTest {
    @Mock
    private ReactionRepository reactionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReactionServiceImpl reactionService;

    @Test
    public void findReactionByUserAndAdvertisement() {
        List<Reaction> reactions = createReactions();
        User user = reactions.get(1).getUser();
        Advertisement ad = reactions.get(1).getAdvertisement();
        Mockito.when(reactionRepository.findAll()).thenReturn(reactions);

        Reaction result = reactionService.findByUserAdvertisement(user, ad);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(reactions.get(1), result);
    }

    private List<Reaction> createReactions() {
        User user1 = new User();
        User user2 = new User();
        user1.setCity("Voronezh");
        user1.setRating(12L);
        user1.setEmail("er@mail.ru");
        user1.setName("jj");
        user1.setPhoneNumber("86486374657");
        user2.setCity("Moscow");
        user2.setRating(4L);
        user2.setEmail("hello@mail.ru");
        user2.setName("olo");
        user2.setPhoneNumber("83564738452");
        Advertisement ad1 = new Advertisement();
        Advertisement ad2 = new Advertisement();
        ad1.setTitle("jjjjjjjjj");
        ad1.setDescription("hi everyone");
        ad2.setTitle("ko");
        ad2.setDescription("kokoko");
        ad1.setUser(user1);
        ad2.setUser(user2);
        Reaction reaction1 = new Reaction();
        Reaction reaction2 = new Reaction();
        Reaction reaction3 = new Reaction();
        reaction1.setUser(user1);
        reaction1.setAdvertisement(ad1);
        reaction2.setUser(user2);
        reaction2.setAdvertisement(ad1);
        reaction3.setUser(user1);
        reaction3.setAdvertisement(ad2);
        return List.of(reaction1, reaction2, reaction3);
    }
}
