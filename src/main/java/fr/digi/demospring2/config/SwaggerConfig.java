package fr.digi.demospring2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger/OpenAPI pour la documentation de l'API
 */
@Configuration
public class SwaggerConfig {

    /**
     * Bean de configuration OpenAPI pour personnaliser la documentation Swagger
     *
     * @return OpenAPI configuration personnalisée
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Recensement - Villes et Départements")
                        .description("Cette API fournit des données sur les villes et départements français. " +
                                "Elle permet de consulter, créer, modifier et supprimer des informations " +
                                "concernant les villes et départements."));
    }
}