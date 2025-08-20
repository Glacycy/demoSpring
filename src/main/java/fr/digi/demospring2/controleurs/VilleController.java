package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.entities.Ville;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private List<Ville> villes = new ArrayList<>();

    //constructor
    public VilleController() {
        villes.add(new Ville("Paris", 2161000));
        villes.add(new Ville("Marseille", 861635));
        villes.add(new Ville("Lyon", 515695));
        villes.add(new Ville("Toulouse", 471941));
        villes.add(new Ville("Nice", 342637));
        villes.add(new Ville("Nantes", 309346));
        villes.add(new Ville("Montpellier", 285121));
        villes.add(new Ville("Strasbourg", 280966));
        villes.add(new Ville("Bordeaux", 254436));
        villes.add(new Ville("Lille", 232741));
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        boolean villeExiste = villes.stream()
                .anyMatch(ville -> ville.getNom().equalsIgnoreCase(nouvelleVille.getNom()));

        if (villeExiste) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La ville existe déjà");
        }

        villes.add(nouvelleVille);

        return ResponseEntity.ok("Ville insérée avec succès");
    }



}
