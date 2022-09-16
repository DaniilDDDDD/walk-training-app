package web.fiiit.dataservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.exception.JwtAuthenticationException;

import java.util.Objects;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {


    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(auth -> auth instanceof TokenAuthentication)
                .cast(TokenAuthentication.class)
                .map(jwtTokenProvider::validateToken)
                .filter(Objects::isNull);
    }

}
