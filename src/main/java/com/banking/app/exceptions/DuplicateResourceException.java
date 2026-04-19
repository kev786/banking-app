package com.banking.app.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public static DuplicateResourceException accountNumber(String number) {
        return new DuplicateResourceException("Un compte avec le numéro « " + number + " » existe déjà.");
    }

    public static DuplicateResourceException bankCode(String code) {
        return new DuplicateResourceException("Une banque avec le code « " + code + " » existe déjà.");
    }
}
