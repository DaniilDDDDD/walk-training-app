package web.fiiit.dataservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Data;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.dto.data.DataAdd;
import web.fiiit.dataservice.dto.data.DataUpdate;
import web.fiiit.dataservice.dto.error.ExceptionResponse;
import web.fiiit.dataservice.service.DataService;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@Tag(name = "Data", description = "Data's operations")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @Operation(
            summary = "Add data",
            description = "Add user's data for provided period of time"
    )
    public Mono<ServerResponse> create(
            @NotNull ServerRequest serverRequest) {

        Mono<DataAdd> data = serverRequest.bodyToMono(DataAdd.class);

        return data
                .flatMap(
                        dataService::create
                )
                .flatMap(
                        d -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .bodyValue(d)
                )
                .onErrorResume(
                        throwable -> ServerResponse
                                .status(HttpStatus.BAD_REQUEST)
                                .bodyValue(
                                        new ExceptionResponse(
                                                throwable.getMessage()
                                        )
                                )
                );
    }

    @Operation(
            summary = "Get data",
            description = "Get user's data for provided period of time"
    )
    public Mono<ServerResponse> get(
            @NotNull ServerRequest request
    ) {

        Optional<String> ownerId = request.queryParam("ownerId");

        Optional<String> startTime = request.queryParam("startTime");
        Optional<String> endTime = request.queryParam("endTime");

        Long start = startTime.map(Long::parseLong).orElse(0L);
        Long end = startTime.map(Long::parseLong).orElse(new Date().getTime());


        if (ownerId.isEmpty()) {
            return ServerResponse.badRequest()
                    .bodyValue(
                            new ExceptionResponse(
                                    "'ownerId' is not provided!"
                            )
                    ).flux();
        }

        return dataService.getAllOwnerDataInPeriod(
                        Long.parseLong(ownerId.get()),
                        start,
                        end
                )
                .flatMap(
                        d -> ServerResponse
                                .ok()
                                .bodyValue(d)
                );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update data",
            description = "Update user's data with provided id"
    )
    public Mono<ServerResponse> update(
            @PathVariable(name = "id") String id,
            @RequestBody DataUpdate dataUpdate
    ) {
        Mono<Data> data = dataService.update(id, dataUpdate);
        return data
                .flatMap(
                        d -> ServerResponse
                                .ok()
                                .bodyValue(d)
                )
                .onErrorResume(
                        throwable -> ServerResponse
                                .status(HttpStatus.BAD_REQUEST)
                                .bodyValue(new ExceptionResponse(
                                        throwable.getMessage()
                                ))
                );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete data",
            description = "Delete data by id and authenticated user id"
    )
    public Mono<ServerResponse> delete(
            @PathVariable(name = "id") String id,
            @AuthenticationPrincipal Mono<Authentication> authentication
    ) {

        return authentication
                .flatMap(
                        auth -> dataService.deleteDataById(
                                id,
                                ((Token) auth.getPrincipal()).getOwnerId()
                        )
                ).flatMap(
                        i -> ServerResponse
                                .status(HttpStatus.NO_CONTENT)
                                .bodyValue(
                                        "Data " + id + " is deleted!"
                                )
                )
                .onErrorResume(
                        throwable -> ServerResponse
                                .badRequest()
                                .bodyValue(
                                        new ExceptionResponse(
                                                throwable.getMessage()
                                        )
                                )
                );
    }

    @DeleteMapping("")
    @Operation(
            summary = "Delete data",
            description = "Delete all user's data or only for provided period of time"
    )
    public Mono<ServerResponse> delete(
            @RequestParam(name = "startTime", required = false) Long startTime,
            @RequestParam(name = "endTime", required = false) Long endTime,
            @AuthenticationPrincipal Mono<Authentication> authentication
    ) {

        if (startTime != null && endTime != null) {

            return authentication
                    .flatMap(
                            auth -> dataService.deleteAllDataInPeriod(
                                    ((Token) auth.getPrincipal()).getOwnerId(),
                                    startTime,
                                    endTime
                            ).next()
                    )
                    .flatMap(
                            id -> ServerResponse
                                    .status(HttpStatus.NO_CONTENT)
                                    .bodyValue(
                                            "Data for period from " +
                                                    new Date(startTime)
                                                            .toInstant()
                                                            .atZone(ZoneId.systemDefault())
                                                            .toLocalDate() +
                                                    " to " +
                                                    new Date(endTime)
                                                            .toInstant()
                                                            .atZone(ZoneId.systemDefault())
                                                            .toLocalDate() +
                                                    " is deleted!"
                                    )
                    )
                    .onErrorResume(
                            throwable -> ServerResponse
                                    .status(HttpStatus.NO_CONTENT)
                                    .bodyValue(
                                            new ExceptionResponse(
                                                    throwable.getMessage()
                                            )
                                    )
                    );

        } else {

            return authentication
                    .flatMap(
                            auth -> dataService.deleteAllOwnerData(
                                    ((Token) auth.getPrincipal()).getOwnerId()
                            ).next()
                    )
                    .flatMap(
                            id -> ServerResponse
                                    .status(HttpStatus.NO_CONTENT)
                                    .bodyValue(
                                            "Data " + id + " is deleted!"
                                    )
                    )
                    .onErrorResume(
                            throwable -> ServerResponse
                                    .status(HttpStatus.NO_CONTENT)
                                    .bodyValue(
                                            new ExceptionResponse(
                                                    throwable.getMessage()
                                            )
                                    )
                    );
        }
    }

}
