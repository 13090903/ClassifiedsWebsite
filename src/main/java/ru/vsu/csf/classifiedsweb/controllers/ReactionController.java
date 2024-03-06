package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;
import ru.vsu.csf.classifiedsweb.services.ReactionService;
import ru.vsu.csf.classifiedsweb.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping("/react/{advertisement}")
    public String createUser(Principal principal, @PathVariable("advertisement") Advertisement advertisement) {
        log.info("User {} reacted to advertisement {}", principal.getName(), advertisement.getTitle());
        reactionService.create(principal, advertisement);
        return "redirect:/advertisements/{advertisement}";
    }
}
