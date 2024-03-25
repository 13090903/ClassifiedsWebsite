package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;
import ru.vsu.csf.classifiedsweb.services.ReactionService;
import ru.vsu.csf.classifiedsweb.services.UserService;
import ru.vsu.csf.classifiedsweb.util.pairs.UserPlace;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final ReactionService reactionService;
    @Autowired
    private final AdvertisementService advertisementService;

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
        if (!userService.createUser(user)) {
            return "redirect:/registration";
        }
        log.info("Register user {}", user);
        return "redirect:/login";
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user,Principal principal, Model model) {
        User userCurr = userService.getUserByPrincipal(principal);
        Set<Long> responses = new HashSet<>();
        List<Advertisement> advertisementList = reactionService.findAdvertisementsByUserId(advertisementService.getUserByPrincipal(principal).getId());
        for (Advertisement ad : advertisementList) {
            responses.add(ad.getId());
        }
        model.addAttribute("responses", responses);
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
        List<User> users1 = userService.findAllSortedByRating();
        List<User> users = new ArrayList<>();
        for (User u : users1) {
            if (u.isActive()) {
                users.add(u);
            }
        }
        User user = userService.getUserByPrincipal(principal);
        for (int number = 0; number < users.size(); number++) {
            usersPlaces.add(new UserPlace(users.get(number), (long) (number+1)));
        }
        model.addAttribute("usersPlaces", usersPlaces);
        model.addAttribute("user", user);
        return "user-rating";
    }

    @GetMapping("/users/{id}/edit")
    public String userEdit(@PathVariable(value = "id") long userID, Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable(value = "id") long userID, @RequestParam("file1") MultipartFile file1, User user, Model model) throws IOException {
        userService.update(userID, user, file1);
        return "redirect:/profile";
    }
}
