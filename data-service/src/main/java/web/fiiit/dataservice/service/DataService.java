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

@Service
public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Mono<Data> create(DataAdd addData) {

        Mono<Data> data = dataRepository.findDataByOwnerIdAndStartTimeAndEndTime(
                addData.getOwnerId(),
                addData.getStartTime(),
                addData.getEndTime()
        ).next();

        return data
                .flatMap(t -> Mono.error(new MongoException("Data already exists!")))
                .switchIfEmpty(
                        dataRepository.save(
                                Data.builder()
                                        .ownerId(addData.getOwnerId())
                                        .text(addData.getText())
                                        .startTime(addData.getStartTime())
                                        .endTime(addData.getEndTime())
                                        .build()
                        )
                )
                .cast(Data.class);
    }

    public Flux<Data> getAllOwnerDataInPeriod(
            Long ownerId, Long start, Long end) {
        return dataRepository.findDataByOwnerIdAndStartTimeAfterAndEndTimeBefore(
                ownerId,
                new Date(start),
                new Date(end)
        );
    }

    public Mono<Data> update(String id, DataUpdate updateData) {

        Mono<Data> data = dataRepository.findDataById(id);

        return data.flatMap(
                        t -> dataRepository.save(
                                Data.builder()
                                        .id(t.getId())
                                        .ownerId(t.getOwnerId())
                                        .text(updateData.getText())
                                        .startTime(updateData.getStartTime())
                                        .endTime(updateData.getEndTime())
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

    public Flux<Long> deleteAllDataInPeriod(
            Long ownerId, Long start, Long end
    ) {

        Flux<Long> deletedIds = dataRepository.deleteDataByOwnerIdAndStartTimeAfterAndEndTimeBefore(
                ownerId, new Date(start), new Date(end)
        );

        return deletedIds.switchIfEmpty(
                Flux.error(new MongoException("No such data!"))
        );
    }

    public Flux<Long> deleteAllOwnerData(Long ownerId) {

        Flux<Long> dataIds = dataRepository.deleteAllByOwnerId(ownerId);

        return dataIds.switchIfEmpty(
                Flux.error(new MongoException("No such data!"))
        );
    }

    public Mono<Long> deleteDataById(String dataId, Long ownerId) {
        Mono<Long> data = dataRepository.deleteDataByIdAndOwnerId(dataId, ownerId);

        return data.switchIfEmpty(
                Mono.error(new MongoException("No such data!"))
        );
    }

}
