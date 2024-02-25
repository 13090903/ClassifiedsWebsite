package ru.vsu.csf.classifiedsweb.services;

import ru.vsu.csf.classifiedsweb.models.Advertisement;

public interface AdvertisementService {
    Advertisement findById(Long id);

    Iterable<Advertisement> findAll();

    void create(Advertisement advertisement);

    void update(Long advertisementId, Advertisement advertisement);

    void deleteById(Long id);
}
