package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.docs.VilleApiDoc;
import fr.digi.demospring2.dto.VilleDTO;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.exceptions.FunctionalException;
import fr.digi.demospring2.mappers.VilleDepartementMapper;
import fr.digi.demospring2.repositories.VilleRepository;
import fr.digi.demospring2.services.DepartementService;
import fr.digi.demospring2.services.VilleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villes")
public class VilleController implements VilleApiDoc {

    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementService departementService;

    @Autowired
    private VilleDepartementMapper mapper;

    @Autowired
    private Validator validator;

    /**
     * GET /villes - Méthode qui retourne la liste paginée des villes
     */
    @GetMapping
    public ResponseEntity<Page<VilleDTO>> getVilles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Ville> villes = villeService.extractVillesPaginated(pageable);
        Page<VilleDTO> villesDTO = villes.map(mapper::toVilleDTO);
        return ResponseEntity.ok(villesDTO);
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
     * GET /villes/recherche/nom/{prefix} - Recherche des villes dont le nom commence par prefix
     * @param prefix préfixe du nom de ville
     * @return Liste des villes correspondantes
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @GetMapping("/recherche/nom/{prefix}")
    public ResponseEntity<List<VilleDTO>> getVillesCommencantPar(@PathVariable String prefix) throws FunctionalException {
        List<Ville> villes = villeService.findVillesStartingWith(prefix);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * GET /villes/population/min/{min} - Villes avec population supérieure à min
     * @param min population minimale
     * @return Liste des villes correspondantes
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @GetMapping("/population/min/{min}")
    public ResponseEntity<List<VilleDTO>> getVillesPopulationMin(@PathVariable int min) throws FunctionalException {
        if (min < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = villeService.findVillesPopulationMin(min);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * GET /villes/population?min={min}&max={max} - Villes avec population entre min et max
     * @param min population minimale
     * @param max population maximale
     * @return Liste des villes correspondantes
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @GetMapping("/population")
    public ResponseEntity<List<VilleDTO>> getVillesPopulationEntre(
            @RequestParam int min,
            @RequestParam int max) throws FunctionalException {

        if (min < 0 || max < 0 || min > max) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = villeService.findVillesPopulationBetween(min, max);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * GET /villes/departement/{id}/population/min/{min} - Villes d'un département avec population > min
     * @param id id du département
     * @param min population minimale
     * @return Liste des villes correspondantes
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @GetMapping("/departement/{id}/population/min/{min}")
    public ResponseEntity<List<VilleDTO>> getVillesDepartementPopulationMin(
            @PathVariable int id,
            @PathVariable int min) throws FunctionalException {

        if (min < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = villeService.findVillesDepartementPopulationMin(id, min);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * GET /villes/departement/{id}/population?min={min}&max={max} - Villes d'un département avec population entre min et max
     * @param id id du département
     * @param min population minimale
     * @param max population maximale
     * @return Liste des villes correspondantes
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @GetMapping("/departement/{id}/population")
    public ResponseEntity<List<VilleDTO>> getVillesDepartementPopulationEntre(
            @PathVariable int id,
            @RequestParam int min,
            @RequestParam int max) throws FunctionalException {

        if (min < 0 || max < 0 || min > max) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = villeService.findVillesDepartementPopulationBetween(id, min, max);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * GET /villes/departement/{id}/top/{n} - Les n villes les plus peuplées d'un département
     * @param id id du département
     * @param n nombre de villes à retourner
     * @return Liste des villes correspondantes
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @GetMapping("/departement/{id}/top/{n}")
    public ResponseEntity<List<VilleDTO>> getTopNVillesDepartement(
            @PathVariable int id,
            @PathVariable int n) throws FunctionalException {

        if (n <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = villeService.findTopNVillesDepartement(id, n);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * POST /villes - Méthode qui ajoute une ville
     * @param villeDTO données de la ville à créer
     * @param bindingResult résultat de la validation
     * @return Message de confirmation ou d'erreur
     * @throws FunctionalException si les règles métier ne sont pas respectées
     */
    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDTO villeDTO, BindingResult bindingResult) throws FunctionalException {

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

        villeService.insertVille(nouvelleVille);
        return ResponseEntity.ok("Ville insérée avec succès");
    }

    /**
     * PUT /villes/{id} - Méthode qui modifie une ville
     * @param id id de la ville à modifier
     * @param villeDTO nouvelles données de la ville
     * @return Message de confirmation ou d'erreur
     * @throws FunctionalException si les règles métier ne sont pas respectées
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @RequestBody VilleDTO villeDTO) throws FunctionalException {

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

            villeService.modifierVille(villeExistante);
            return ResponseEntity.ok("Ville modifiée avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /villes/{id} - Méthode qui supprime une ville
     * @param id id de la ville à supprimer
     * @return Message de confirmation ou d'erreur
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

    /**
     * GET /villes/export/csv?min={min} - Exporte au format CSV les villes avec population > min
     * @param min population minimale
     * @param response HttpServletResponse pour configurer la réponse HTTP
     * @throws IOException si erreur d'écriture
     * @throws FunctionalException si aucune ville trouvée
     */
    @GetMapping("/export/csv")
    public void exportVillesCSV(
            @RequestParam int min,
            HttpServletResponse response) throws IOException, FunctionalException {

        if (min < 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La population minimale ne peut pas être négative");
            return;
        }

        List<Ville> villes = villeService.findVillesPopulationMin(min);

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"villes_population_min_" + min + ".csv\"");

        PrintWriter writer = response.getWriter();

        writer.println("nom_ville,nombre_habitants,code_departement,nom_departement");

        for (Ville ville : villes) {
            writer.printf("%s,%d,%s,%s%n",
                    ville.getNom(),
                    ville.getNbHabitants(),
                    ville.getDepartement().getCode(),
                    ville.getDepartement().getNom()
            );
        }

        writer.flush();
        writer.close();
    }
}