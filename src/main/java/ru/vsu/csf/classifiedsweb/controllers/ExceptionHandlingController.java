package ru.vsu.csf.classifiedsweb.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.vsu.csf.classifiedsweb.util.exceptions.AdvertisementNotFoundException;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(AdvertisementNotFoundException.class)
    public String advertisementNotFoundHandle() {
        return "exception";
    }
}
