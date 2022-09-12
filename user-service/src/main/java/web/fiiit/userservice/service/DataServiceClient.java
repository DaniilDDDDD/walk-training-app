package web.fiiit.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import web.fiiit.userservice.dto.token.SendToken;
import web.fiiit.userservice.dto.token.SendTokenResponse;
import web.fiiit.userservice.exceptions.DataServiceResponseError;
import web.fiiit.userservice.model.Token;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class DataServiceClient {

    private WebClient webClient;

    private final String baseUri;

    private final String token;

    @PostConstruct
    private void init() {
        webClient = WebClient.builder()
                .baseUrl(baseUri)
                .defaultHeader("Authorisation", "Bearer_" + token)
                .build();
    }


    // TODO: change creation of inter service communication token
    @Autowired
    public DataServiceClient(
            @Value("${dataServiceBaseUri}") String baseUri,
            @Value("${dataServiceSecret}") String token
    ) {
        this.baseUri = baseUri;
        this.token = token;
    }

    public void sendToken(Token token) {
        SendTokenResponse sendTokenResponse = webClient.post().uri("api/token")
                .body(new SendToken(token.getValue(), token.getOwner().getId()),
                        SendToken.class)
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
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)))
                .filter(throwable -> throwable instanceof DataServiceResponseError)
                .block();
    }



}
