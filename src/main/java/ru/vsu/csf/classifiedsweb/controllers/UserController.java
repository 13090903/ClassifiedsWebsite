package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.services.UserService;
import ru.vsu.csf.classifiedsweb.util.pairs.UserPlace;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(User user) {
        log.info("Register user {}", user);
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user,Principal principal, Model model) {
        User userCurr = userService.getUserByPrincipal(principal);
        model.addAttribute("userC", userCurr);
        model.addAttribute("user", user);
        model.addAttribute("advertisements", user.getAdvertisements());
        return "user-information";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/users/rating")
    public String usersRating(Principal principal, Model model) {
        List<UserPlace> usersPlaces = new ArrayList<>();
        List<User> users = userService.findAllSortedByRating();
        User user = userService.getUserByPrincipal(principal);
        for (int number = 0; number < users.size(); number++) {
            usersPlaces.add(new UserPlace(users.get(number), (long) (number+1)));
        }
        model.addAttribute("usersPlaces", usersPlaces);
        model.addAttribute("user", user);
        return "user-rating";
    }
}
