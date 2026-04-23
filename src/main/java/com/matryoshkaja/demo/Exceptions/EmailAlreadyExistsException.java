package com.matryoshkaja.demo.Exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email: " + email +  "is already assigned to another account");
    }
}
