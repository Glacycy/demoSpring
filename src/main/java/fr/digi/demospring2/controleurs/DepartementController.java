package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.services.DepartementService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementController {

    @Autowired
    private DepartementService dptService;

    @Autowired
    private Validator validator;

    /**
     * GET /departements - Retourne la liste des départements
     */
    @GetMapping
    public List<Departement> getDepartements() {
        return dptService.extractDepartements();
    }

    /**
     * GET /departements/{id} - Retourne un département par son Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable int id) {
        Departement dpt = dptService.extractDepartement(id);

        if (dpt != null) {
            return ResponseEntity.ok(dpt);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /departements/code/{code} - Retourne un département par son code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Departement> getDepartementByCode(@PathVariable String code) {
        Departement dpt = dptService.extractDepartementByCode(code);

        if (dpt != null) {
            return ResponseEntity.ok(dpt);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /departements - Insère un département
     */
    @PostMapping
    public ResponseEntity<String> ajouterDepartement(@Valid @RequestBody Departement newDpt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            String messageErreur = String.join(", ", erreurs);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErreur);
        }

        Departement dptExistant = dptService.extractDepartementByCode(newDpt.getCode());
        if (dptExistant != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le département existe déjà");
        }

        dptService.insertDepartement(newDpt);
        return ResponseEntity.ok("Département inséré avec succès");
    }

    /**
     * PUT /departements/{id} - Modifie un département
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierDepartement(@PathVariable int id, @RequestBody Departement dptEdited) {
        Set<ConstraintViolation<Departement>> violations = validator.validate(dptEdited);

        if (!violations.isEmpty()) {
            List<String> erreurs = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            String messageErreur = String.join(", ", erreurs);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErreur);
        }

        Departement dptExistant = dptService.extractDepartement(id);
        if (dptExistant != null) {
            dptService.modifierDepartement(id, dptEdited);
            return ResponseEntity.ok("Département modifié avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * DELETE /departements/{id} - Supprime un département
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerDepartement(@PathVariable int id) {
        Departement dptExistant = dptService.extractDepartement(id);
        if (dptExistant != null) {
            dptService.supprimerDepartement(id);
            return ResponseEntity.ok("Département supprimé avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /departements/{id}/plus-grandes-villes/{n} - Retourne les n plus grandes villes d'un département
     */
    @GetMapping("/{id}/plus-grandes-villes/{n}")
    public ResponseEntity<List<Ville>> getNPlusGrandesVilles(@PathVariable int id, @PathVariable int n) {
        Departement dpt = dptService.extractDepartement(id);

        if (dpt == null) {
            return ResponseEntity.notFound().build();
        }

        if (n <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = dptService.getNPlusGrandesVilles(id, n);
        return ResponseEntity.ok(villes);

    }
    /**
     * GET /departements/{id}/villes-population?min={min}&max={max} - Retourne les villes d'un département ayant une population comprise entre deux valeurs
     */
    @GetMapping("/{id}/villes-population")
    public ResponseEntity<List<Ville>> getVillesParPopulation(
            @PathVariable int id,
            @RequestParam int min,
            @RequestParam int max) {

        Departement dpt = dptService.extractDepartement(id);

        if (dpt == null) {
            return ResponseEntity.notFound().build();
        }

        if (min < 0 || max < 0 || min > max) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = dptService.getVillesParPopulation(id, min, max);
        return ResponseEntity.ok(villes);
    }




}
