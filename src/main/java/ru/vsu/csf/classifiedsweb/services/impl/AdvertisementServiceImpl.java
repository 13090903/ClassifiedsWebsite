package ru.vsu.csf.classifiedsweb.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.repositories.AdvertisementRepository;
import ru.vsu.csf.classifiedsweb.services.AdvertisementService;
import ru.vsu.csf.classifiedsweb.util.exceptions.AdvertisementNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public Advertisement findById(Long id) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        return advertisement.orElseThrow(AdvertisementNotFoundException::new);
    }

    @Override
    public Iterable<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    @Override
    public void create(Advertisement advertisement) {
        log.info("Creating new {}", advertisement);
        advertisementRepository.save(advertisement);
    }

    @Override
    public void update(Long advertisementId, Advertisement advertisement) {
        log.info("Updating {}", advertisement);
        Advertisement prevAdvertisement =  advertisementRepository.findById(advertisementId).orElseThrow();
        prevAdvertisement.setTitle(advertisement.getTitle());
        prevAdvertisement.setDescription(advertisement.getDescription());
        advertisementRepository.save(prevAdvertisement);
    }

    @Override
    public void deleteById(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(AdvertisementNotFoundException::new);
        log.info("Deleting {}", advertisement);
        advertisementRepository.delete(advertisement);
    }
}
