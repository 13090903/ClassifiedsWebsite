package ru.vsu.csf.classifiedsweb.util.exceptions;

public class AdvertisementNotFoundException extends RuntimeException{
    public AdvertisementNotFoundException() {
        super("Advertisement not found");
    }
}
