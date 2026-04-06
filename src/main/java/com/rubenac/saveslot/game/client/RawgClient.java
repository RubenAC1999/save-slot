package com.rubenac.saveslot.game.client;

import com.rubenac.saveslot.game.dto.RawgGameDetail;
import com.rubenac.saveslot.game.dto.RawgGameSummary;
import com.rubenac.saveslot.game.dto.RawgSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RawgClient {
    private final WebClient rawgWebClient;

    @Value("${RAWG_API}")
    private String apiKey;

    public List<RawgGameSummary> searchGames(String query) {
        return rawgWebClient.get()
                .uri("/games?key={key}&search={query}", apiKey, query)
                .retrieve()
                .bodyToMono(RawgSearchResponse.class)
                .map(RawgSearchResponse::results)
                .block();
    }

    public RawgGameDetail getGameDetail(Integer rawgId) {
        return rawgWebClient.get()
                .uri("/games/{id}?key={key}", rawgId, apiKey)
                .retrieve()
                .bodyToMono(RawgGameDetail.class)
                .block();
    }
}
