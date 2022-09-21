package web.fiiit.dataservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Data;
import web.fiiit.dataservice.document.Token;
import web.fiiit.dataservice.dto.data.DataAdd;
import web.fiiit.dataservice.dto.data.DataUpdate;
import web.fiiit.dataservice.dto.error.ExceptionResponse;
import web.fiiit.dataservice.service.DataService;

import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("api/data")
@Tag(name = "Data", description = "Data's operations")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("")
    @Operation(
            summary = "Add data",
            description = "Add user's data for provided period of time"
    )
    public Mono<ServerResponse> create(DataAdd addData) {
        Mono<Data> data = dataService.create(addData);

        return data
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

    @GetMapping("")
    @Operation(
            summary = "Get data",
            description = "Get user's data for provided period of time"
    )
    public Flux<ServerResponse> get(
            @RequestParam(name = "ownerId") Long ownerId,
            @RequestParam(name = "startTime") Long startTime,
            @RequestParam(name = "endTime") Long endTime
    ) {
        Flux<Data> data = dataService.getAllOwnerDataInPeriod(
                ownerId, startTime, endTime);

        return data
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
