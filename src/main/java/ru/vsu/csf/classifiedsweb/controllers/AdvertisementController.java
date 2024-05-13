package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.csf.classifiedsweb.enums.AdvertisementState;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;
import ru.vsu.csf.classifiedsweb.services.ReactionService;
import ru.vsu.csf.classifiedsweb.services.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private ReactionService reactionService;
    @Autowired
    private UserService userService;

    @GetMapping("/advertisements")
    public String advertisements(@RequestParam(name = "city", required = false) String city, Principal principal, Model model) {
        List<Advertisement> ads;
        Iterable<Advertisement> advertisements = advertisementService.findAll();
        if (city != null && !city.equals("")) {
            ads = advertisementService.findAllByCity(city);
        } else {
            ads = new ArrayList<>();
            for (Advertisement ad : advertisements) {
                if (ad.getState() != AdvertisementState.COMPLETED) {
                    ads.add(ad);
                }
            }
        }
        ads.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        if (city != null && !city.equals("")) {
            for (Advertisement ad : advertisements) {
                if (ad.getState() == AdvertisementState.COMPLETED && ad.getUser().getCity().equalsIgnoreCase(city)) {
                    ads.add(ad);
                }
            }
        } else {
            for (Advertisement ad : advertisements) {
                if (ad.getState() == AdvertisementState.COMPLETED) {
                    ads.add(ad);
                }
            }
        }
        Set<Long> responses = new HashSet<>();
        List<Advertisement> advertisementList = reactionService.findAdvertisementsByUserId(advertisementService.getUserByPrincipal(principal).getId());
        for (Advertisement ad : advertisementList) {
            responses.add(ad.getId());
        }
        model.addAttribute("advertisements", ads);
        model.addAttribute("responses", responses);
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));
        return "advertisements";
    }

    @GetMapping("/advertisements/{id}")
    public String advertisementDescription(@PathVariable(value = "id") long advertisementID, Principal principal, Model model) {
        Iterable<Advertisement> advertisements = advertisementService.findAll();
        Set<Long> responses = new HashSet<>();
        List<Advertisement> advertisementList = reactionService.findAdvertisementsByUserId(advertisementService.getUserByPrincipal(principal).getId());
        for (Advertisement ad : advertisementList) {
            responses.add(ad.getId());
        }
        model.addAttribute("responses", responses);
        model.addAttribute("advertisement", advertisementService.findById(advertisementID));
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));

        return "advertisement-description";
    }

    @GetMapping("/advertisements/{id}/complete")
    public String advertisementCompletionPage(@PathVariable(value = "id") long advertisementID, Principal principal, Model model) {
        model.addAttribute("advertisement", advertisementService.findById(advertisementID));
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));

        return "advertisement-complete";
    }

    @PostMapping("/advertisements/{id}/complete")
    public String advertisementCompletion(@PathVariable(value = "id") long advertisementID, @RequestParam(value = "reactedUsers")Long[] users, Principal principal) {
        User user = advertisementService.getUserByPrincipal(principal);
        //TODO: maybe better do it in service
        if (user.equals(advertisementService.findById(advertisementID).getUser())) {
            advertisementService.complete(advertisementID);
            for (Long uId : users) {
                userService.addRatingById(uId, 1L);
            }
        }
        return "redirect:/advertisements";
    }

    @GetMapping("/advertisements/add")
    public String advertisementAdd(Principal principal, Model model) {
        model.addAttribute("advertisements", advertisementService.findAll());
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));

        return "advertisement-add";
    }

    @PostMapping("/advertisements/add")
    public String addAdvertisement(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                   @RequestParam("file3") MultipartFile file3, Advertisement advertisement, Principal principal, Model model) throws IOException {
        advertisementService.create(principal, advertisement, file1, file2, file3);
        return "redirect:/advertisements";
    }

    @GetMapping("/advertisements/{id}/edit")
    public String advertisementEdit(@PathVariable(value = "id") long advertisementID, Principal principal, Model model) {
        model.addAttribute("advertisement", advertisementService.findById(advertisementID));
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));
        return "advertisement-edit";
    }

    @PostMapping("/advertisements/{id}/edit")
    public String editAdvertisement(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                    @RequestParam("file3") MultipartFile file3, @PathVariable(value = "id") long advertisementID, Advertisement advertisement, Model model) throws IOException {
        advertisementService.update(advertisementID, advertisement, file1, file2, file3);
        return "redirect:/advertisements";
    }

    @PostMapping("advertisements/{id}/remove")
    public String removeAdvertisement(@PathVariable(value = "id") long advertisementID, Model model) {
        advertisementService.deleteById(advertisementID);
        return "redirect:/advertisements";
    }
}
