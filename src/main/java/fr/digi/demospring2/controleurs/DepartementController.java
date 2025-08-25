package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.dto.DepartementDTO;
import fr.digi.demospring2.dto.VilleDTO;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.mappers.VilleDepartementMapper;
import fr.digi.demospring2.repositories.DepartementRepository;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementController {

    @Autowired
    private DepartementService dptService;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private VilleDepartementMapper mapper;

    @Autowired
    private Validator validator;

    /**
     * GET /departements - Retourne la liste des départements avec leurs villes
     */
    @GetMapping
    public List<DepartementDTO> getDepartements() {
        List<Departement> departements = dptService.extractDepartements();
        return mapper.toDepartementDTO(departements);
    }

    /**
     * GET /departements/{id} - Retourne un département par son Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartementDTO> getDepartementById(@PathVariable int id) {
        Departement dpt = dptService.extractDepartement(id);

        if (dpt != null){
            DepartementDTO dto = mapper.toDepartementDTO(dpt);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /departements/code/{code} - Retourne un département par son code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<DepartementDTO> getDepartementByCode(@PathVariable String code) {
        Departement dpt = dptService.extractDepartementByCode(code);

        if (dpt != null){
            DepartementDTO dto = mapper.toDepartementDTO(dpt);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /departements - Insère un département
     */
    @PostMapping
    public ResponseEntity<String> ajouterDepartement(@Valid @RequestBody DepartementDTO dptDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            String messageErreur = String.join(", ", erreurs);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErreur);
        }

        Optional<Departement> dptExistant = departementRepository.findByCode(dptDTO.getCode());
        if (dptExistant.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le département existe déjà");
        }

        Departement newDpt = new Departement(dptDTO.getCode(), dptDTO.getNom());
        departementRepository.save(newDpt);
        return ResponseEntity.ok("Département inséré avec succès");
    }

    /**
     * PUT /departements/{id} - Modifie un département
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierDepartement(@PathVariable int id, @RequestBody DepartementDTO dptDTO) {
        Set<ConstraintViolation<DepartementDTO>> violations = validator.validate(dptDTO);

        if (!violations.isEmpty()) {
            List<String> erreurs = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            String messageErreur = String.join(", ", erreurs);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErreur);
        }

        Optional<Departement> dptOptional = departementRepository.findById(id);
        if (dptOptional.isPresent()) {
            Departement dptExistant = dptOptional.get();
            mapper.updateDepartementFromDTO(dptExistant, dptDTO);
            departementRepository.save(dptExistant);
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
        if (departementRepository.existsById(id)) {
            departementRepository.deleteById(id);
            return ResponseEntity.ok("Département supprimé avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /departements/{id}/plus-grandes-villes/{n} - Retourne les n plus grandes villes d'un département
     */
    @GetMapping("/{id}/plus-grandes-villes/{n}")
    public ResponseEntity<List<VilleDTO>> getNPlusGrandesVilles(@PathVariable int id, @PathVariable int n) {
        if (!departementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (n <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = dptService.getNPlusGrandesVilles(id, n);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }

    /**
     * GET /departements/{id}/villes-population?min={min}&max={max} - Retourne les villes d'un département ayant une population comprise entre deux valeurs
     */
    @GetMapping("/{id}/villes-population")
    public ResponseEntity<List<VilleDTO>> getVillesParPopulation(
            @PathVariable int id,
            @RequestParam int min,
            @RequestParam int max) {

        if (!departementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (min < 0 || max < 0 || min > max) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = dptService.getVillesParPopulation(id, min, max);
        List<VilleDTO> villesDTO = mapper.toVilleDTO(villes);
        return ResponseEntity.ok(villesDTO);
    }
}