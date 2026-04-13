package com.matryoshkaja.demo.Exceptions;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long adminId) {
        super("Admin with id " + adminId + " was not found");
    }
}
