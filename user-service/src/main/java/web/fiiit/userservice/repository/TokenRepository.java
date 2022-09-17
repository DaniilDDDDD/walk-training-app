package web.fiiit.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.fiiit.userservice.model.Token;
import web.fiiit.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenById(Long id);

    Optional<Token> findTokenByValue(String value);

    List<Token> findAllByOwner(User owner);

    void deleteAllByOwnerUsername(String username);

}
