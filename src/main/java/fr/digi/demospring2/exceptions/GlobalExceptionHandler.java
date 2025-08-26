package fr.digi.demospring2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Gestionnaire global des exceptions pour l'application
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestionnaire pour les exceptions fonctionnelles
     * @param ex l'exception fonctionnelle
     * @param request la requête web
     * @return ResponseEntity avec le message d'erreur et le statut HTTP 400
     */
    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<String> handleFunctionalException(FunctionalException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Gestionnaire pour les exceptions génériques non prévues
     * @param ex l'exception
     * @param request la requête web
     * @return ResponseEntity avec un message d'erreur générique et le statut HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur inattendue s'est produite : " + ex.getMessage());
    }
}