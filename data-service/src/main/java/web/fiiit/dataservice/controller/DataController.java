package web.fiiit.dataservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Data;
import web.fiiit.dataservice.dto.data.AddData;
import web.fiiit.dataservice.dto.error.ExceptionResponse;
import web.fiiit.dataservice.service.DataService;

import javax.management.InstanceAlreadyExistsException;
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
    public Mono<ServerResponse> create(AddData addData) {
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

    @PutMapping("")
    @Operation(
            summary = "Update data",
            description = "Update user's data with provided id"
    )
    public Mono<ServerResponse> update(
            @RequestParam(name = "id") Long id
    ) {
        Mono<Data> data =
    }

    @DeleteMapping("")
    @Operation(
            summary = "Delete data",
            description = "Delete user's data for provided period of time"
    )
    public Mono<ServerResponse> delete(
            @RequestParam(name = "ownerId") Long ownerId,
            @RequestParam(name = "startTime") Long startTime,
            @RequestParam(name = "endTime") Long endTime
    ) {
        Mono<Long> dataIds = dataService.deleteAllDataInPeriod(
                ownerId, startTime, endTime).next();

        return dataIds
                .flatMap(
                        id -> ServerResponse
                                .status(HttpStatus.NO_CONTENT)
                                .bodyValue(
                                        "User " + ownerId + " data for period from " +
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
                .switchIfEmpty(
                        ServerResponse
                                .status(HttpStatus.NO_CONTENT)
                                .bodyValue(
                                        "No such data was in database!"
                                )
                );
    }

    @DeleteMapping("")
    @Operation(
            summary = "Delete data",
            description = "Delete user's data"
    )
    public Mono<ServerResponse> deleteAllUserData(
            @RequestParam(name = "ownerId") Long ownerId
    ) {
        Mono<Long> dataIds = dataService.deleteAllOwnerData(ownerId).next();

        return dataIds
                .flatMap(
                        id -> ServerResponse
                                .status(HttpStatus.NO_CONTENT)
                                .bodyValue(
                                        "User " + ownerId + " data is deleted!"
                                )
                )
                .switchIfEmpty(
                        ServerResponse
                                .status(HttpStatus.NO_CONTENT)
                                .bodyValue(
                                        "No such data was in database!"
                                )
                );
    }
}
