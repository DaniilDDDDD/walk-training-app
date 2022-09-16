package web.fiiit.dataservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.exception.JwtAuthenticationException;
import web.fiiit.dataservice.service.TokenService;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    private final TokenService tokenService;

    @Autowired
    public JwtTokenProvider(
            TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Authentication getEmptyAuthentication(String tokenValue) {
        return new TokenAuthentication(tokenValue);
    }

    public Authentication getAuthenticationFromValidToken(Token token) {
        return new TokenAuthentication(
                token.getValue(),
                token
        );
    }

    public Authentication validateToken(TokenAuthentication tokenAuthentication) {
        Optional<Token> token = tokenService.findByValue(
                (String) tokenAuthentication.getCredentials()
        ).blockOptional();

        if (token.isEmpty() || token.get().getExpirationTime().before(new Date())) {
            return null;
        }
        return new TokenAuthentication(
                token.get().getValue(),
                token.get()
        );
    }

}
