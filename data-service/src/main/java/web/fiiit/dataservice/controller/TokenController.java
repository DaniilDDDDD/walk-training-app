package web.fiiit.dataservice.controller;

import com.mongodb.MongoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.dto.error.ExceptionResponse;
import web.fiiit.dataservice.dto.token.AddToken;
import web.fiiit.dataservice.dto.token.TokenResponse;
import web.fiiit.dataservice.service.TokenService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/token")
@Tag(name = "Token", description = "Tokens' operations")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("")
    @Operation(
            summary = "Create token",
            description = "Add user's token with provided token's values"
    )
    public Mono<ServerResponse> add(
            @Valid @RequestBody AddToken addToken
    ) {
        Mono<Token> token = tokenService.create(addToken);

        return token
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

    @DeleteMapping("")
    @Operation(
            summary = "Delete",
            description = "Delete token (by tokenId if userId is not provided)"
    )
    public Mono<ServerResponse> delete(
            @Nullable @RequestParam(name = "userId") Long userId,
            @Nullable @RequestParam(name = "tokenId") Long tokenId
    ) {
        if (tokenId != null) {
            Mono<Long> token = tokenService.deleteByRootId(tokenId);
            return token
                    .flatMap(
                            t -> ServerResponse
                                    .status(HttpStatus.NO_CONTENT)
                                    .bodyValue("Token with id " + t + " is deleted!")
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

        if (userId != null) {
            Flux<Long> tokens = tokenService.deleteAllByOwnerId(userId);
            return tokens.next().flatMap(
                    t -> ServerResponse
                            .status(HttpStatus.NO_CONTENT)
                            .bodyValue("Tokens of user " + t + " deleted!")
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
                                "\"userId\" or \"tokenId\" are not provided!"
                        )
                );
    }


}
