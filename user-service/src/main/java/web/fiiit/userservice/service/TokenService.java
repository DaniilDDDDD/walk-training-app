package web.fiiit.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.fiiit.userservice.model.Token;
import web.fiiit.userservice.model.User;
import web.fiiit.userservice.repository.TokenRepository;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token addToken(Token token) {
        return tokenRepository.save(token);
    }

    public void deleteAllUserTokens(String name) {
        tokenRepository.deleteAllByOwnerUsername(name);
    }
}
