package web.fiiit.dataservice.repository;

import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;

@Repository
public interface TokenRepository extends ReactiveMongoRepository<Token, Long> {

    Mono<Token> findTokenById(Long id);

    Mono<Token> findTokenByRootId(Long id);

    Mono<Token> findTokenByValue(String value);

    Flux<Token> findTokenByRootIdOrValue(Long id, String value);

    Flux<Token> findAllByOwnerId(Long id);

    Mono<Long> deleteTokenByRootId(Long id);

    Flux<Long> deleteAllByOwnerId(Long id);

}
