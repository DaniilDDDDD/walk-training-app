package web.fiiit.dataservice.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import web.fiiit.dataservice.controller.DataController;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class DataRouter {

    @Bean
    public RouterFunction<ServerResponse> route(DataController dataController) {
        return RouterFunctions
                .route(
                        GET("api/data"),
                        dataController::get
                )
                .andRoute(
                        POST("api/data").and(accept(MediaType.APPLICATION_JSON)),
                        dataController::create
                )
                .andRoute(
                        PUT("api/data/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        dataController::update
                )
                .andRoute(
                        DELETE("api/data/{id}"),
                        dataController::deleteById
                )
                .andRoute(
                        DELETE("api/data"),
                        dataController::delete
                );
    }

}
