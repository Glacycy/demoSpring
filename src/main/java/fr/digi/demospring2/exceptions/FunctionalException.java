package fr.digi.demospring2.exceptions;

/**
 * Exception fonctionnelle pour gérer les erreurs métier de l'application
 */
public class FunctionalException extends Exception {

    /**
     * Constructeur avec message d'erreur
     * @param message le message d'erreur
     */
    public FunctionalException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message d'erreur et cause
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public FunctionalException(String message, Throwable cause) {
        super(message, cause);
    }
}