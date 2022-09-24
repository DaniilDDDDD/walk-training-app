package web.fiiit.dataservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.security.JwtAuthenticationConverter;
import web.fiiit.dataservice.security.JwtAuthenticationManager;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            JwtAuthenticationConverter converter,
            JwtAuthenticationManager manager
    ) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(manager);

        filter.setServerAuthenticationConverter(converter);

        return http
                .httpBasic().disable()
                .csrf().disable()
//                .cors().disable()
                .formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (swe, e) ->
                                Mono.fromRunnable(
                                        () -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                                )
                )
                .accessDeniedHandler(
                        (swe, e) ->
                                Mono.fromRunnable(
                                        () -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
                                )
                )
                .and()
                    .authorizeExchange()
                        .pathMatchers("api/token*").permitAll()
                        .pathMatchers("api/data*").permitAll()
                    .anyExchange().authenticated()
                .and()
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
