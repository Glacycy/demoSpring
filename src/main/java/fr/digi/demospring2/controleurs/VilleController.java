package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.entities.Ville;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private List<Ville> villes = new ArrayList<>();

    //constructor
    public VilleController() {
        villes.add(new Ville(1,"Paris", 2161000));
        villes.add(new Ville(2, "Marseille", 861635));
        villes.add(new Ville(3,"Lyon", 515695));
        villes.add(new Ville(4,"Toulouse", 471941));
        villes.add(new Ville(5,"Nice", 342637));
        villes.add(new Ville(6,"Nantes", 309346));
        villes.add(new Ville(7,"Montpellier", 285121));
        villes.add(new Ville(8,"Strasbourg", 280966));
        villes.add(new Ville(9,"Bordeaux", 254436));
        villes.add(new Ville(10,"Lille", 232741));
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable int id) {
        Optional<Ville> ville = villes.stream()
                .filter(v -> v.getId() == id)
                .findFirst();

        if (ville.isPresent()) {
            return ResponseEntity.ok(ville.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        boolean idExiste = villes.stream()
                .anyMatch(ville -> ville.getId() == nouvelleVille.getId());

        if (idExiste) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Une ville avec cet ID existe déjà");
        }

        boolean nomExiste = villes.stream()
                .anyMatch(ville -> ville.getNom().equalsIgnoreCase(nouvelleVille.getNom()));

        if (nomExiste) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La ville existe déjà");
        }

        villes.add(nouvelleVille);
        return ResponseEntity.ok("Ville insérée avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @RequestBody Ville villeModifiee) {
        Optional<Ville> villeExistante = villes.stream()
                .filter(v -> v.getId() == id)
                .findFirst();

        if (villeExistante.isPresent()) {
            Ville ville = villeExistante.get();
            ville.setNom(villeModifiee.getNom());
            ville.setNbHabitants(villeModifiee.getNbHabitants());

            return ResponseEntity.ok("Ville modifiée avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        boolean villeSuprimee = villes.removeIf(ville -> ville.getId() == id);

        if (villeSuprimee) {
            return ResponseEntity.ok("Ville supprimée avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
