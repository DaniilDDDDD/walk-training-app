package web.fiiit.dataservice.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    public Authentication getAuthentication(String tokenValue) throws JwtException {
        return new TokenAuthentication(tokenValue);
    }

    public Authentication validateToken(TokenAuthentication tokenAuthentication) throws JwtAuthenticationException {
        Optional<Token> token = tokenService.findByValue(
                (String) tokenAuthentication.getCredentials()
        ).blockOptional();

        if (token.isEmpty() || token.get().getExpirationTime().before(new Date())) {
            throw new JwtAuthenticationException(
                    "Jwt token is expired or invalid!",
                    "Authorization"
            );
        }
        return new TokenAuthentication(
                (String) tokenAuthentication.getCredentials(),
                token.get()
        );

    }

}
