package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    @Autowired
    private final AdvertisementService advertisementService;
    @GetMapping("/")
    public String home(Principal principal, Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));
        return "home";
    }
}
