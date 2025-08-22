package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.dto.VilleDTO;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.mappers.VilleDepartementMapper;
import fr.digi.demospring2.services.VilleService;
import fr.digi.demospring2.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villes")
public class VilleController {

    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementService departementService;

    @Autowired
    private VilleDepartementMapper mapper;

    @Autowired
    private Validator validator;

    /**
     * GET /villes - Méthode qui retourne la liste des villes
     */
    @GetMapping
    public List<VilleDTO> getVilles() {
        List<Ville> villes = villeService.extractVilles();
        return mapper.toVilleDTO(villes);
    }

    /**
     * GET /villes/{id} - Méthode qui retourne une ville par son id
     */
    @GetMapping("/{id}")
    public ResponseEntity<VilleDTO> getVilleById(@PathVariable int id) {
        Ville ville = villeService.extractVille(id);

        if (ville != null) {
            VilleDTO villeDTO = mapper.toVilleDTO(ville);
            return ResponseEntity.ok(villeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /villes/nom/{nom} - Méthode qui retourne une ville par son nom
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<VilleDTO> getVilleByNom(@PathVariable String nom) {
        Ville ville = villeService.extractVille(nom);

        if (ville != null) {
            VilleDTO villeDTO = mapper.toVilleDTO(ville);
            return ResponseEntity.ok(villeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /villes - Méthode qui ajoute une ville
     */
    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDTO villeDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            String messageErreur = String.join(", ", erreurs);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErreur);
        }

        Ville villeExistante = villeService.extractVille(villeDTO.getNom());
        if (villeExistante != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La ville existe déjà");
        }

        Departement departement = departementService.extractDepartement(villeDTO.getDepartementId());
        if (departement == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le département avec l'ID " + villeDTO.getDepartementId() + " n'existe pas");
        }

        Ville nouvelleVille = new Ville(villeDTO.getNom(), villeDTO.getNbHabitants(), departement);

        try {
            villeService.insertVille(nouvelleVille);
            return ResponseEntity.ok("Ville insérée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * PUT /villes/{id} - Méthode qui modifie une ville
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @RequestBody VilleDTO villeDTO) {

        Set<ConstraintViolation<VilleDTO>> violations = validator.validate(villeDTO);

        if (!violations.isEmpty()) {
            List<String> erreurs = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            String messageErreur = String.join(", ", erreurs);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErreur);
        }

        Ville villeExistante = villeService.extractVille(id);
        if (villeExistante != null) {
            mapper.updateVilleFromDTO(villeExistante, villeDTO);

            if (villeDTO.getDepartementId() != null) {
                Departement nouveauDepartement = departementService.extractDepartement(villeDTO.getDepartementId());
                if (nouveauDepartement == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Le département avec l'ID " + villeDTO.getDepartementId() + " n'existe pas");
                }
                villeExistante.setDepartement(nouveauDepartement);
            }

            villeService.modifierVille(id, villeExistante);
            return ResponseEntity.ok("Ville modifiée avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /villes/{id} - Méthode qui supprime une ville
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        Ville ville = villeService.extractVille(id);

        if (ville != null) {
            villeService.supprimerVille(id);
            return ResponseEntity.ok("Ville supprimée avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}