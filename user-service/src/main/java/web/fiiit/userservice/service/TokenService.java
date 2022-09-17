package web.fiiit.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.fiiit.userservice.model.Token;
import web.fiiit.userservice.model.User;
import web.fiiit.userservice.repository.TokenRepository;
import web.fiiit.userservice.repository.UserRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public Token create(Token token) throws EntityExistsException {
        Optional<Token> tokenData = tokenRepository.findTokenByValue(token.getValue());

        if (tokenData.isPresent()) {
            throw new EntityExistsException("Token with this value already exists!");
        }
        return tokenRepository.save(token);
    }

    public void deleteAllUserTokens(String name) throws EntityNotFoundException {

        Optional<User> user = userRepository.findUserByUsername(name);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("No user with provided username!");
        }

        tokenRepository.deleteAllByOwnerUsername(name);
    }
}
