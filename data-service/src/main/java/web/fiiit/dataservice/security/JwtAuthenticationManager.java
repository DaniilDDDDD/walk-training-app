package web.fiiit.dataservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {


    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    public JwtAuthenticationManager(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(auth -> auth instanceof TokenAuthentication)
                .cast(TokenAuthentication.class)
                .flatMap(jwtAuthenticationProvider::validateToken)
                .filter(Objects::nonNull);
    }

}
