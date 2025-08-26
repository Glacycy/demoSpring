package fr.digi.demospring2.services;

import fr.digi.demospring2.dto.DepartementApiDTO;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.repositories.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service pour gérer les appels vers l'API externe
 * et mettre à jour les données en base de données
 */
@Service
public class ApiService {

    /**
     * URL de l'API des départements du gouvernement français
     */
    private static final String DEPARTEMENTS_API_URL = "https://geo.api.gouv.fr/departements";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DepartementRepository departementRepository;

    /**
     * Récupère tous les départements depuis l'API externe
     * @return Liste des départements depuis l'API
     * @throws RestClientException si l'appel API échoue
     */
    public List<DepartementApiDTO> getDepartementsFromApi() throws RestClientException {
        System.out.println("Appel de l'API : " + DEPARTEMENTS_API_URL);

        try {
            DepartementApiDTO[] departementsArray = restTemplate.getForObject(
                    DEPARTEMENTS_API_URL,
                    DepartementApiDTO[].class
            );

            if (departementsArray == null) {
                throw new RestClientException("Réponse vide de l'API");
            }

            List<DepartementApiDTO> departements = Arrays.asList(departementsArray);
            System.out.println("Récupération de " + departements.size() + " départements depuis l'API");

            return departements;

        } catch (RestClientException e) {
            System.err.println("Erreur lors de l'appel API : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Met à jour les noms des départements en base de données
     * avec les données récupérées de l'API externe
     */
    public void updateDepartementsFromApi() {
        try {
            List<DepartementApiDTO> departementsApi = getDepartementsFromApi();

            int countUpdated = 0;
            int countNotFound = 0;
            int countCreated = 0;

            for (DepartementApiDTO dptApi : departementsApi) {
                if (dptApi.getCode() == null || dptApi.getNom() == null) {
                    System.out.println("Données incomplètes pour le département : " + dptApi);
                    continue;
                }

                Optional<Departement> dptExistant = departementRepository.findByCode(dptApi.getCode());

                if (dptExistant.isPresent()) {
                    Departement departement = dptExistant.get();

                    if (!Objects.equals(departement.getNom(), dptApi.getNom())) {
                        String ancienNom = departement.getNom() != null ? departement.getNom() : "null";

                        System.out.println("Mise à jour du département " + dptApi.getCode() +
                                " : '" + ancienNom + "' → '" + dptApi.getNom() + "'");

                        departement.setNom(dptApi.getNom());
                        departementRepository.save(departement);
                        countUpdated++;
                    }
                } else {
                    System.out.println("Création du département : " + dptApi.getCode() + " - " + dptApi.getNom());

                    Departement nouveauDepartement = new Departement(dptApi.getCode(), dptApi.getNom());
                    departementRepository.save(nouveauDepartement);
                    countCreated++;
                }
            }

            System.out.println("Mise à jour terminée :");
            System.out.println("- Départements mis à jour : " + countUpdated);
            System.out.println("- Départements créés : " + countCreated);
            System.out.println("- Départements non trouvés en base : " + countNotFound);

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des départements : " + e.getMessage());
            e.printStackTrace();
        }
    }
}