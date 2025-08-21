package fr.digi.demospring2.config;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.services.DepartementService;
import fr.digi.demospring2.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Classe pour initialiser la base de données avec des données de test
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartementService departementService;

    @Autowired
    private VilleService villeService;

    @Override
    public void run(String... args) throws Exception {
        if (!departementService.extractDepartements().isEmpty()) {
            return;
        }

        Departement paris = new Departement("75", "Paris");
        Departement bouches = new Departement("13", "Bouches-du-Rhône");
        Departement rhone = new Departement("69", "Rhône");
        Departement hauteGaronne = new Departement("31", "Haute-Garonne");
        Departement alpesMaritimes = new Departement("06", "Alpes-Maritimes");
        Departement loireAtlantique = new Departement("44", "Loire-Atlantique");
        Departement herault = new Departement("34", "Hérault");
        Departement basRhin = new Departement("67", "Bas-Rhin");
        Departement gironde = new Departement("33", "Gironde");
        Departement nord = new Departement("59", "Nord");

        departementService.insertDepartement(paris);
        departementService.insertDepartement(bouches);
        departementService.insertDepartement(rhone);
        departementService.insertDepartement(hauteGaronne);
        departementService.insertDepartement(alpesMaritimes);
        departementService.insertDepartement(loireAtlantique);
        departementService.insertDepartement(herault);
        departementService.insertDepartement(basRhin);
        departementService.insertDepartement(gironde);
        departementService.insertDepartement(nord);

        Departement parisCreated = departementService.extractDepartementByCode("75");
        Departement bouchesCreated = departementService.extractDepartementByCode("13");
        Departement rhoneCreated = departementService.extractDepartementByCode("69");
        Departement hauteGaronneCreated = departementService.extractDepartementByCode("31");
        Departement alpesMaritimesCreated = departementService.extractDepartementByCode("06");
        Departement loireAtlantiqueCreated = departementService.extractDepartementByCode("44");
        Departement heraultCreated = departementService.extractDepartementByCode("34");
        Departement basRhinCreated = departementService.extractDepartementByCode("67");
        Departement girondeCreated = departementService.extractDepartementByCode("33");
        Departement nordCreated = departementService.extractDepartementByCode("59");

        villeService.insertVille(new Ville("Paris", 2161000, parisCreated));
        villeService.insertVille(new Ville("Marseille", 861635, bouchesCreated));
        villeService.insertVille(new Ville("Lyon", 515695, rhoneCreated));
        villeService.insertVille(new Ville("Toulouse", 471941, hauteGaronneCreated));
        villeService.insertVille(new Ville("Nice", 342637, alpesMaritimesCreated));
        villeService.insertVille(new Ville("Nantes", 309346, loireAtlantiqueCreated));
        villeService.insertVille(new Ville("Montpellier", 285121, heraultCreated));
        villeService.insertVille(new Ville("Strasbourg", 280966, basRhinCreated));
        villeService.insertVille(new Ville("Bordeaux", 254436, girondeCreated));
        villeService.insertVille(new Ville("Lille", 232741, nordCreated));

        villeService.insertVille(new Ville("Aix-en-Provence", 143006, bouchesCreated));
        villeService.insertVille(new Ville("Cannes", 74152, alpesMaritimesCreated));
        villeService.insertVille(new Ville("Antibes", 75820, alpesMaritimesCreated));
        villeService.insertVille(new Ville("Villeurbanne", 148543, rhoneCreated));
        villeService.insertVille(new Ville("Colomiers", 39764, hauteGaronneCreated));

        System.out.println("Données d'initialisation créées avec succès !");
    }
}