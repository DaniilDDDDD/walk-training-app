package web.fiiit.dataservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.repository.TokenRepository;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

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
        );
    }

    public Mono<Token> findById(Long id) {
        return tokenRepository.findTokenById(id);
    }

    public Mono<Token> findByRootId(Long id) {
        return tokenRepository.findTokenByRootId(id);
    }

    public Flux<Token> allUserTokens(Long userId) {
        return tokenRepository.findAllByOwnerId(userId);
    }

    public Mono<Token> findByValue(String value){
        return tokenRepository.findTokenByValue(value);
    }

}
