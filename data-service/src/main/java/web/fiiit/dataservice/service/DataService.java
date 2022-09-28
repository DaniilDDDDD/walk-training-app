package web.fiiit.dataservice.service;


import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Data;
import web.fiiit.dataservice.dto.data.DataAdd;
import web.fiiit.dataservice.dto.data.DataUpdate;
import web.fiiit.dataservice.repository.DataRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Mono<Data> create(DataAdd addData, Long ownerId) {

        Mono<Data> data = dataRepository.findDataByOwnerIdAndStartTimeAndEndTime(
                ownerId,
                addData.getStartTime(),
                addData.getEndTime()
        ).next();

        return data
                .flatMap(t -> Mono.error(new MongoException("Data already exists!")))
                .switchIfEmpty(
                        dataRepository.save(
                                Data.builder()
                                        .ownerId(ownerId)
                                        .text(addData.getText())
                                        .startTime(addData.getStartTime())
                                        .endTime(addData.getEndTime())
                                        .build()
                        )
                )
                .cast(Data.class);
    }


    public Mono<Data> getDataById(String id, Long ownerId) {
        return dataRepository.deleteDataByIdAndOwnerId(id, ownerId);
    }

    public Flux<Data> getAllOwnerDataInPeriod(
            Long ownerId, Long start, Long end) {
        return dataRepository.findDataByOwnerIdAndStartTimeAfterAndEndTimeBefore(
                ownerId,
                new Date(start),
                new Date(end)
        );
    }


    public Mono<Data> updateOwnerData(Long ownerId, String id, DataUpdate updateData) {

        Mono<Data> data = dataRepository.findDataByIdAndOwnerId(id, ownerId);

        return data
                .flatMap(
                        t -> dataRepository.save(
                                Data.builder()
                                        .id(t.getId())
                                        .ownerId(t.getOwnerId())
                                        .text(
                                                updateData.getText() != null ?
                                                        updateData.getText() : t.getText()
                                        )
                                        .startTime(
                                                updateData.getStartTime() != null ?
                                                        updateData.getStartTime() : t.getStartTime()
                                        )
                                        .endTime(
                                                updateData.getEndTime() != null ?
                                                        updateData.getEndTime() : t.getEndTime()
                                        )
                                        .build()
                        )
                )
                .switchIfEmpty(
                        Mono.error(
                                new MongoException(
                                        "No Data with id " + id + "!"
                                )
                        )
                );
    }

    public Mono<Data> update(String id, DataUpdate updateData) {

        Mono<Data> data = dataRepository.findDataById(id);

        return data.flatMap(
                        t -> dataRepository.save(
                                Data.builder()
                                        .id(t.getId())
                                        .ownerId(t.getOwnerId())
                                        .text(
                                                updateData.getText() != null ?
                                                        updateData.getText() : t.getText()
                                        )
                                        .startTime(
                                                updateData.getStartTime() != null ?
                                                        updateData.getStartTime() : t.getStartTime()
                                        )
                                        .endTime(
                                                updateData.getEndTime() != null ?
                                                        updateData.getEndTime() : t.getEndTime()
                                        )
                                        .build()
                        )
                )
                .switchIfEmpty(
                        Mono.error(
                                new MongoException(
                                        "No Data with id " + id + "!"
                                )
                        )
                );
    }


    public Mono<Data> deleteDataById(String dataId, Long ownerId) {
        Mono<Data> data = dataRepository.deleteDataByIdAndOwnerId(dataId, ownerId);

        return data.switchIfEmpty(
                Mono.error(new MongoException("No such data!"))
        );
    }

    public Flux<Data> deleteAllDataInPeriod(
            Long ownerId, Long start, Long end
    ) {

        Flux<Data> deletedIds = dataRepository.deleteDataByOwnerIdAndStartTimeAfterAndEndTimeBefore(
                ownerId, new Date(start), new Date(end)
        );

        return deletedIds.switchIfEmpty(
                Flux.error(new MongoException("No such data!"))
        );
    }

}
