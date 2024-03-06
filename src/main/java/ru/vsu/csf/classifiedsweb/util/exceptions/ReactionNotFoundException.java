package ru.vsu.csf.classifiedsweb.util.exceptions;

public class ReactionNotFoundException extends RuntimeException{
    public ReactionNotFoundException() {
        super("Reaction not found");
    }
}
