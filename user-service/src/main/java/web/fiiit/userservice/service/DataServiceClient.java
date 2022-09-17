package web.fiiit.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import web.fiiit.userservice.dto.token.SendToken;
import web.fiiit.userservice.dto.token.SendTokenResponse;
import web.fiiit.userservice.exception.DataServiceResponseError;
import web.fiiit.userservice.model.Token;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class DataServiceClient {

    private WebClient webClient;

    // TODO: change creation of inter service communication token
    @Value("${dataServiceBaseUri}")
    private String baseUri;

    @Value("${dataServiceApiVersion}")
    private String apiVersion;

    @Value("${dataServiceToken}")
    private String token;

    @PostConstruct
    private void init() {
        webClient = WebClient.builder()
                .baseUrl(baseUri + apiVersion + "/")
                .defaultHeader("Authorization", "Bearer_" + token)
                .build();
    }

    public void add(Token token) {
        webClient.post().uri("api/token")
                .bodyValue(new SendToken(
                                token.getId(),
                                token.getValue(),
                                token.getOwner().getId(),
                                token.getExpirationTime()
                        ))
                .retrieve()
                .onStatus(
                        HttpStatus::is5xxServerError,
                        response -> Mono.error(
                                new DataServiceResponseError(
                                        "Internal server error of data service!",
                                        response.rawStatusCode()
                                )
                        )
                )
                .bodyToMono(SendTokenResponse.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(3))
                        .filter(throwable -> throwable instanceof DataServiceResponseError));
    }

    public void delete(long id, boolean byUserId) {
        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("api/token")
                        .queryParam(
                                (byUserId) ? "userId" : "tokenId",
                                id
                        )
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatus::is5xxServerError,
                        response -> Mono.error(
                                new DataServiceResponseError(
                                        "Internal server error of data service!",
                                        response.rawStatusCode()
                                )
                        )
                )
                .bodyToMono(SendTokenResponse.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(3))
                        .filter(throwable -> throwable instanceof DataServiceResponseError));
    }



}
