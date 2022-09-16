package web.fiiit.dataservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
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
    ) throws Exception {

        AuthenticationWebFilter filter = new AuthenticationWebFilter(manager);

        filter.setServerAuthenticationConverter(converter);

        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeExchange().anyExchange().authenticated()
                .and()
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
