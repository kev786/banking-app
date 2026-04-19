package com.banking.app.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException account(Long id) {
        return new ResourceNotFoundException("Compte introuvable avec l'identifiant : " + id);
    }

    public static ResourceNotFoundException bank(Long id) {
        return new ResourceNotFoundException("Banque introuvable avec l'identifiant : " + id);
    }
}
