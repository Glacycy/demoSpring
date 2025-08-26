package fr.digi.demospring2.controleurs;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.digi.demospring2.docs.DepartementApiDoc;
import fr.digi.demospring2.dto.DepartementDTO;
import fr.digi.demospring2.dto.VilleDTO;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.exceptions.FunctionalException;
import fr.digi.demospring2.mappers.VilleDepartementMapper;
import fr.digi.demospring2.repositories.DepartementRepository;
import fr.digi.demospring2.services.DepartementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementController implements DepartementApiDoc {

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
    public ResponseEntity<List<VilleDTO>> getNPlusGrandesVilles(@PathVariable int id, @PathVariable int n) throws FunctionalException {
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
            @RequestParam int max) throws FunctionalException {

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

    /**
     * GET /departements/{code}/export/pdf - Exporte les informations d'un département au format PDF
     * @param code code du département
     * @param response HttpServletResponse pour configurer la réponse HTTP
     * @throws DocumentException si erreur lors de la création du PDF
     * @throws IOException si erreur d'écriture
     */
    @GetMapping("/{code}/export/pdf")
    public void exportDepartementPDF(
            @PathVariable String code,
            HttpServletResponse response) throws DocumentException, IOException {

        Departement departement = dptService.extractDepartementByCode(code);

        if (departement == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Département non trouvé avec le code : " + code);
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"departement_" + code + ".pdf\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        try {
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Département : " + departement.getNom(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            Paragraph codeInfo = new Paragraph();
            codeInfo.add(new Phrase("Code du département : ", boldFont));
            codeInfo.add(new Phrase(departement.getCode(), normalFont));
            codeInfo.setSpacingAfter(10);
            document.add(codeInfo);

            Paragraph nomInfo = new Paragraph();
            nomInfo.add(new Phrase("Nom du département : ", boldFont));
            nomInfo.add(new Phrase(departement.getNom(), normalFont));
            nomInfo.setSpacingAfter(20);
            document.add(nomInfo);

            Paragraph villesTitle = new Paragraph("Liste des villes", boldFont);
            villesTitle.setSpacingAfter(15);
            document.add(villesTitle);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            PdfPCell headerNom = new PdfPCell(new Phrase("Nom de la ville", boldFont));
            PdfPCell headerPopulation = new PdfPCell(new Phrase("Population", boldFont));
            headerNom.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerPopulation.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerNom.setPadding(8);
            headerPopulation.setPadding(8);
            table.addCell(headerNom);
            table.addCell(headerPopulation);

            List<Ville> villes = departement.getVilles();

            if (villes.isEmpty()) {
                PdfPCell noVilleCell = new PdfPCell(new Phrase("Aucune ville enregistrée", normalFont));
                noVilleCell.setColspan(2);
                noVilleCell.setPadding(8);
                noVilleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(noVilleCell);
            } else {
                villes.sort((v1, v2) -> Integer.compare(v2.getNbHabitants(), v1.getNbHabitants()));

                for (Ville ville : villes) {
                    PdfPCell nomCell = new PdfPCell(new Phrase(ville.getNom(), normalFont));
                    PdfPCell populationCell = new PdfPCell(new Phrase(String.valueOf(ville.getNbHabitants()), normalFont));
                    nomCell.setPadding(8);
                    populationCell.setPadding(8);
                    table.addCell(nomCell);
                    table.addCell(populationCell);
                }
            }

            document.add(table);

            Paragraph footer = new Paragraph();
            footer.setSpacingBefore(30);
            footer.add(new Phrase("Total de villes : ", boldFont));
            footer.add(new Phrase(String.valueOf(villes.size()), normalFont));
            footer.add(new Phrase("\nPopulation totale du département : ", boldFont));

            long totalPopulation = villes.stream().mapToLong(Ville::getNbHabitants).sum();
            footer.add(new Phrase(totalPopulation + " habitants", normalFont));

            document.add(footer);

        } finally {
            document.close();
        }
    }
}