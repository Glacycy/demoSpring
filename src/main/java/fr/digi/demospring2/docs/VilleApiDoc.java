package fr.digi.demospring2.docs;

import fr.digi.demospring2.dto.VilleDTO;
import fr.digi.demospring2.exceptions.FunctionalException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

/**
 * Interface de documentation Swagger pour les endpoints des villes
 */
@Tag(name = "Villes", description = "API de gestion des villes")
public interface VilleApiDoc {

    /**
     * Documentation pour GET /villes
     */
    @Operation(summary = "Retourne la liste paginée des villes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Page de villes au format JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))})
    })
    ResponseEntity<Page<VilleDTO>> getVilles(
            @Parameter(description = "Numéro de la page (commence à 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page", example = "10")
            @RequestParam(defaultValue = "10") int size);

    /**
     * Documentation pour POST /villes
     */
    @Operation(summary = "Crée une nouvelle ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ville créée avec succès",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400",
                    description = "Données invalides ou ville déjà existante",
                    content = {@Content(mediaType = "text/plain")})
    })
    ResponseEntity<String> ajouterVille(
            @Parameter(description = "Données de la ville à créer", required = true)
            @Valid @RequestBody VilleDTO villeDTO,
            BindingResult bindingResult) throws FunctionalException;

    /**
     * Documentation pour PUT /villes/{id}
     */
    @Operation(summary = "Modifie une ville existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ville modifiée avec succès",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400",
                    description = "Données invalides",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404",
                    description = "Ville non trouvée",
                    content = @Content())
    })
    ResponseEntity<String> modifierVille(
            @Parameter(description = "Identifiant de la ville à modifier",
                    example = "1",
                    required = true)
            @PathVariable int id,
            @Parameter(description = "Nouvelles données de la ville", required = true)
            @RequestBody VilleDTO villeDTO) throws FunctionalException;

    /**
     * Documentation pour DELETE /villes/{id}
     */
    @Operation(summary = "Supprime une ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ville supprimée avec succès",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404",
                    description = "Ville non trouvée",
                    content = @Content())
    })
    ResponseEntity<String> supprimerVille(
            @Parameter(description = "Identifiant de la ville à supprimer",
                    example = "1",
                    required = true)
            @PathVariable int id);
}