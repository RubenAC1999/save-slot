package com.rubenac.saveslot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final String RAWG_BASE_URL = "https://api.rawg.io/api";

    @Value("${RAWG_API}")
    private String API_KEY;

    @Bean
    public WebClient rawgWebClient() {
        return WebClient.builder()
                .baseUrl(RAWG_BASE_URL)
                .build();
    }
}
