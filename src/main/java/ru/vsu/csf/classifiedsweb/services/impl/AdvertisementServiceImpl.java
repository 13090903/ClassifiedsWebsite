package ru.vsu.csf.classifiedsweb.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.csf.classifiedsweb.enums.AdvertisementState;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.Image;
import ru.vsu.csf.classifiedsweb.models.User;
import ru.vsu.csf.classifiedsweb.repositories.AdvertisementRepository;
import ru.vsu.csf.classifiedsweb.repositories.ImageRepository;
import ru.vsu.csf.classifiedsweb.repositories.UserRepository;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;
import ru.vsu.csf.classifiedsweb.util.exceptions.AdvertisementNotFoundException;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    @Override
    public Advertisement findById(Long id) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        return advertisement.orElseThrow(AdvertisementNotFoundException::new);
    }

    @Override
    public Iterable<Advertisement> findAll() {
        return advertisementRepository.findAllByOrderByStateAsc();
    }

    @Override
    public List<Advertisement> findAllByCity(String city) {
        if (city == null) {
            return (List<Advertisement>) findAll();
        }
        Iterable<Advertisement> all = advertisementRepository.findAllByOrderByStateAsc();
        List<Advertisement> searched = new ArrayList<>();
        for (Advertisement ad : all) {
            if (ad.getUser().getCity() != null && ad.getUser().getCity().equalsIgnoreCase(city) && ad.getState() != AdvertisementState.COMPLETED) {
                searched.add(ad);
            }
        }
        return searched;
    }

    @Override
    public void create(Principal principal, Advertisement advertisement, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        advertisement.setUser(getUserByPrincipal(principal));
        advertisement.setState(AdvertisementState.ACTIVE);
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            advertisement.addImageToAdvertisement(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            advertisement.addImageToAdvertisement(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            advertisement.addImageToAdvertisement(image3);
        }
        log.info("Creating new advertisement. Title - {}, Author email - {}", advertisement.getTitle(), advertisement.getUser().getEmail());
        Advertisement prevAdvertisement = advertisementRepository.save(advertisement);
        prevAdvertisement.setPreviewImageId(prevAdvertisement.getImages().get(0).getId());
        advertisementRepository.save(prevAdvertisement);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Override
    public void update(Long advertisementId, Advertisement advertisement, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Advertisement prevAdvertisement = advertisementRepository.findById(advertisementId).orElseThrow(AdvertisementNotFoundException::new);
        prevAdvertisement.setTitle(advertisement.getTitle());
        prevAdvertisement.setDescription(advertisement.getDescription());
//        prevAdvertisement.deleteImages();
//        for (Image image : prevAdvertisement.getImages()) {
//            imageRepository.deleteById(image.getId());
//        }
//        Image image1;
//        Image image2;
//        Image image3;
//        if (file1.getSize() != 0) {
//            image1 = toImageEntity(file1);
//            image1.setPreviewImage(true);
//            prevAdvertisement.addImageToAdvertisement(image1);
//        }
//        if (file2.getSize() != 0) {
//            image2 = toImageEntity(file2);
//            prevAdvertisement.addImageToAdvertisement(image2);
//        }
//        if (file3.getSize() != 0) {
//            image3 = toImageEntity(file3);
//            prevAdvertisement.addImageToAdvertisement(image3);
//        }
        log.info("Updating advertisement. Id - {}", advertisementId);
//        advertisementRepository.save(prevAdvertisement);
//        prevAdvertisement.setPreviewImageId(prevAdvertisement.getImages().get(0).getId());
        advertisementRepository.save(prevAdvertisement);
    }

    @Override
    public void deleteById(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(AdvertisementNotFoundException::new);
        log.info("Deleting {}", advertisement);
        advertisementRepository.delete(advertisement);
    }

    @Override
    public void complete(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(AdvertisementNotFoundException::new);
        advertisement.setState(AdvertisementState.COMPLETED);
        log.info("advertisement {} completed", advertisement);
        advertisementRepository.save(advertisement);
    }
}
