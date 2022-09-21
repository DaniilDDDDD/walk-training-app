package web.fiiit.dataservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.service.TokenService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtAuthenticationProvider {

    private final GrantedAuthority userAuthority = () -> "ROLE_USER";

    private final GrantedAuthority serviceAuthority = () -> "ROLE_SERVICE";

    private final TokenService tokenService;

    @Value("${dataServiceToken}")
    private String dataServiceToken;

    @Autowired
    public JwtAuthenticationProvider(
            TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Authentication getEmptyAuthentication(String tokenValue) {
        return new TokenAuthentication(tokenValue);
    }

    public Mono<Authentication> validateToken(TokenAuthentication tokenAuthentication) {
        Mono<Token> token = tokenService.findByValue(
                (String) tokenAuthentication.getCredentials()
        );

        return token
                .filter(
                        t -> t.getExpirationTime().after(new Date())
                ).map(
                        t -> new TokenAuthentication(
                                t.getValue(),
                                t,
                                Objects.equals(t.getValue(), dataServiceToken) ?
                                        List.of(serviceAuthority) :
                                        List.of(userAuthority)
                        )
                );
    }

}
