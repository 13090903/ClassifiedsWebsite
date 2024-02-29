package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;

    @GetMapping("/advertisements")
    public String advertisements(Principal principal, Model model) {
        Iterable<Advertisement> advertisements = advertisementService.findAll();
        model.addAttribute("advertisements", advertisements);
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));
        return "advertisements";
    }

    @GetMapping("/advertisements/{id}")
    public String advertisementDescription(@PathVariable(value = "id") long advertisementID, Principal principal, Model model) {
        model.addAttribute("advertisement", advertisementService.findById(advertisementID));
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));

        return "advertisement-description";
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
