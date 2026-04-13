package com.matryoshkaja.demo.Exceptions;

public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(Long photoId) {
        super("Photo with id " + photoId + " was not found");
    }
}
