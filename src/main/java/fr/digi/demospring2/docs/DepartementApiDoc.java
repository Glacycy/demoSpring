package fr.digi.demospring2.docs;

import fr.digi.demospring2.dto.DepartementDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Interface de documentation Swagger pour les endpoints des départements
 */
@Tag(name = "Départements", description = "API de gestion des départements")
public interface DepartementApiDoc {

    /**
     * Documentation pour GET /departements
     */
    @Operation(summary = "Retourne la liste de tous les départements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Liste des départements au format JSON",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DepartementDTO.class)))})
    })
    List<DepartementDTO> getDepartements();

    /**
     * Documentation pour GET /departements/{id}
     */
    @Operation(summary = "Retourne un département à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Département au format JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartementDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Département non trouvé",
                    content = @Content())
    })
    ResponseEntity<DepartementDTO> getDepartementById(
            @Parameter(description = "Identifiant du département à récupérer",
                    example = "1",
                    required = true)
            @PathVariable int id);

    /**
     * Documentation pour POST /departements
     */
    @Operation(summary = "Crée un nouveau département")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Département créé avec succès",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400",
                    description = "Données invalides ou département déjà existant",
                    content = {@Content(mediaType = "text/plain")})
    })
    ResponseEntity<String> ajouterDepartement(
            @Parameter(description = "Données du département à créer", required = true)
            @Valid @RequestBody DepartementDTO dptDTO,
            BindingResult bindingResult);

    /**
     * Documentation pour DELETE /departements/{id}
     */
    @Operation(summary = "Supprime un département")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Département supprimé avec succès",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404",
                    description = "Département non trouvé",
                    content = @Content())
    })
    ResponseEntity<String> supprimerDepartement(
            @Parameter(description = "Identifiant du département à supprimer",
                    example = "1",
                    required = true)
            @PathVariable int id);
}