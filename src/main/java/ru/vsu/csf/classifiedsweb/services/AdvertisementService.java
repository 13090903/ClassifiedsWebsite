package ru.vsu.csf.classifiedsweb.services;

import org.springframework.web.multipart.MultipartFile;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.User;

import java.io.IOException;
import java.security.Principal;

public interface AdvertisementService {
    Advertisement findById(Long id);

    Iterable<Advertisement> findAll();

    void create(Principal principal, Advertisement advertisement, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException;

    void update(Long advertisementId, Advertisement advertisement, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException;

    void deleteById(Long id);

    void complete(Long id);

    User getUserByPrincipal(Principal principal);
}
