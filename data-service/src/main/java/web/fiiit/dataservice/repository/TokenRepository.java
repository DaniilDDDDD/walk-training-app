package web.fiiit.dataservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;


@Repository
public interface TokenRepository extends ReactiveMongoRepository<Token, Long> {

    Mono<Boolean> existsTokenByValue(String value);

    Mono<Token> findTokenById(String id);

    Mono<Token> findTokenByRootId(Long id);

    Mono<Token> findTokenByValue(String value);

    Flux<Token> findTokenByRootIdOrValue(Long id, String value);

    Flux<Token> findAllByOwnerId(Long id);

    Mono<Token> deleteTokenByRootId(Long id);

    Flux<Token> deleteAllByOwnerId(Long id);

}
