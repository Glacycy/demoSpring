package fr.digi.demospring2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration des beans pour les appels d'API externe
 */
@Configuration
public class ApiConfig {

    /**
     * Bean RestTemplate pour effectuer les appels HTTP vers les APIs externes
     * @return instance configurée de RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}