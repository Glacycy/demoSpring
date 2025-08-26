package fr.digi.demospring2;

import fr.digi.demospring2.services.ApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//@Profile("cli")
@Component
public class ApiDataLoader implements CommandLineRunner {

    private final ApiService apiService;

    public ApiDataLoader(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Démarrage du chargement des données depuis l'API externe ===");
        try {
            System.out.println("Étape 2 : Mise à jour des départements.");
            apiService.updateDepartementsFromApi();
            System.out.println("=== Traitement terminé avec succès ===");
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
