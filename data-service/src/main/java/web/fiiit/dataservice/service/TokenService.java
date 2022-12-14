package web.fiiit.dataservice.service;

import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.dto.token.TokenAdd;
import web.fiiit.dataservice.repository.TokenRepository;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Value("${dataServiceToken}")
    private String userServiceToken;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @PostConstruct
    public void init() {

        if (Boolean.TRUE.equals(
                tokenRepository.existsTokenByValue(userServiceToken
                ).block())) return;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 100);
        Date expirationDate = calendar.getTime();

        tokenRepository.save(
                Token.builder()
                        .value(userServiceToken)
                        .rootId(-1L)
                        .ownerId(-1L)
                        .expirationTime(expirationDate)
                        .build()
        ).block();
    }

    public Mono<Token> create(TokenAdd addToken) {

        Mono<Token> token = tokenRepository.findTokenByRootIdOrValue(
                addToken.getRootId(),
                addToken.getValue()
        ).next();

        return token
                .flatMap(t -> Mono.error(new MongoException("Token already exists!")))
                .switchIfEmpty(
                        tokenRepository.save(
                                Token.builder()
                                        .rootId(addToken.getRootId())
                                        .ownerId(addToken.getOwnerId())
                                        .value(addToken.getValue())
                                        .expirationTime(addToken.getExpirationTime())
                                        .build()
                        )
                )
                .cast(Token.class);
    }

    public Mono<Token> findById(String id) {
        return tokenRepository.findTokenById(id);
    }

    public Mono<Token> findByRootId(Long id) {
        return tokenRepository.findTokenByRootId(id);
    }

    public Flux<Token> allUserTokens(Long userId) {
        return tokenRepository.findAllByOwnerId(userId);
    }

    public Mono<Token> findByValue(String value) {
        return tokenRepository.findTokenByValue(value);
    }

    public Mono<Token> deleteByRootId(Long id) {

        Mono<Token> token = tokenRepository.deleteTokenByRootId(id);

        return token.switchIfEmpty(
                Mono.error(new MongoException("Token with this id does not exist!"))
        );
    }

    public Flux<Token> deleteAllByOwnerId(Long id) {

        Flux<Token> token = tokenRepository.deleteAllByOwnerId(id);

        return token.switchIfEmpty(
                Mono.error(new MongoException("Tokens with this id do not exist!"))
        );
    }

}
