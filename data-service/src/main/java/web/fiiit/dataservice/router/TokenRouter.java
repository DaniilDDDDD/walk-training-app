package web.fiiit.dataservice.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import web.fiiit.dataservice.controller.TokenController;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class TokenRouter {

    @Bean
    public RouterFunction<ServerResponse> tokenRouterFunction(TokenController tokenController) {
        return RouterFunctions
                .route(
                        POST("api/token").and(accept(MediaType.APPLICATION_JSON)),
                        tokenController::add
                ).andRoute(
                        DELETE("api/token"),
                        tokenController::delete
                );
    }

}
