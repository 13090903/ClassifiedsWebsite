package ru.vsu.csf.classifiedsweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.csf.classifiedsweb.models.Image;
import ru.vsu.csf.classifiedsweb.repositories.ImageRepository;

import java.io.ByteArrayInputStream;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageRepository imageRepository;

    @GetMapping("/images/{id}")
    @ResponseBody
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        log.info("Getting image. Id = {}", id);
        Image image = imageRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
