package ru.vsu.csf.classifiedsweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @GetMapping("/advertisements")
    public String advertisements(Model model) {
        Iterable<Advertisement> advertisements = advertisementService.findAll();
        model.addAttribute("ads", advertisements);
        return "advertisements";
    }

    @GetMapping("/advertisements/{id}")
    public String advertisementDescription(@PathVariable(value = "id") long advertisementID, Model model) {
        model.addAttribute("advertisement", advertisementService.findById(advertisementID));
        return "advertisement-description";
    }

    @GetMapping("/advertisements/add")
    public String advertisementAdd(Model model) {
        model.addAttribute("advertisements", advertisementService.findAll());
        return "advertisement-add";
    }

    @PostMapping("/advertisements/add")
    public String addAdvertisement(Advertisement advertisement, Model model) {
        advertisementService.create(advertisement);
        return "redirect:/advertisements";
    }

    @GetMapping("/advertisements/{id}/edit")
    public String advertisementEdit(@PathVariable(value = "id") long advertisementID, Model model) {
        model.addAttribute("advertisement", advertisementService.findById(advertisementID));
        return "advertisement-edit";
    }

    @PostMapping("/advertisements/{id}/edit")
    public String editAdvertisement(@PathVariable(value = "id") long advertisementID, Advertisement advertisement, Model model) {
        advertisementService.update(advertisementID, advertisement);
        return "redirect:/advertisements";
    }

    @PostMapping("advertisements/{id}/remove")
    public String removeAdvertisement(@PathVariable(value = "id") long advertisementID, Model model) {
        advertisementService.deleteById(advertisementID);
        return "redirect:/advertisements";
    }
}
