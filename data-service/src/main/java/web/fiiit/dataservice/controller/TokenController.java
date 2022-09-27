package web.fiiit.dataservice.controller;

import com.mongodb.MongoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.dto.error.ExceptionResponse;
import web.fiiit.dataservice.dto.token.TokenAdd;
import web.fiiit.dataservice.dto.token.TokenResponse;
import web.fiiit.dataservice.service.TokenService;

import java.util.Optional;

@Component
@Tag(name = "Token", description = "Tokens' operations")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Operation(
            summary = "Create token",
            description = "Add user's token with provided token's values"
    )
    public Mono<ServerResponse> add(
            ServerRequest request
    ) {
        Mono<TokenAdd> token = request.bodyToMono(TokenAdd.class);

        return token
                .flatMap(
                        tokenService::create
                )
                .flatMap(
                        t -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .bodyValue(
                                        new TokenResponse(
                                                t.getId(),
                                                t.getRootId(),
                                                t.getOwnerId(),
                                                t.getValue(),
                                                t.getExpirationTime()
                                        )
                                )
                )
                .onErrorResume(
                        MongoException.class,
                        throwable -> ServerResponse
                                .status(HttpStatus.BAD_REQUEST)
                                .bodyValue(
                                        new ExceptionResponse(
                                                throwable.getMessage()
                                        )
                                )
                );
    }


    @Operation(
            summary = "Delete",
            description = "Delete token (by tokenId if userId is not provided)"
    )
    public Mono<ServerResponse> delete(
            ServerRequest request
    ) {

        Optional<String> userId = request.queryParam("ownerId");
        Optional<String> tokenId = request.queryParam("tokenId");

        if (tokenId.isPresent()) {
            Mono<Token> token = tokenService.deleteByRootId(Long.parseLong(tokenId.get()));
            return token
                    .flatMap(
                            t -> ServerResponse
                                    .noContent().build()
                    )
                    .onErrorResume(
                            throwable -> ServerResponse
                                    .status(HttpStatus.BAD_REQUEST)
                                    .bodyValue(
                                            new ExceptionResponse(
                                                    throwable.getMessage()
                                            )
                                    )
                    );
        }

        if (userId.isPresent()) {
            Flux<Token> tokens = tokenService.deleteAllByOwnerId(Long.parseLong(userId.get()));
            return tokens.next()
                    .flatMap(
                            t -> ServerResponse
                                    .noContent().build()
                    ).onErrorResume(
                            throwable -> ServerResponse
                                    .status(HttpStatus.BAD_REQUEST)
                                    .bodyValue(
                                            new ExceptionResponse(
                                                    throwable.getMessage()
                                            )
                                    )
                    );
        }

        return ServerResponse.badRequest()
                .bodyValue(new ExceptionResponse(
                                "'ownerId' or 'tokenId' are not provided!"
                        )
                );
    }


}
