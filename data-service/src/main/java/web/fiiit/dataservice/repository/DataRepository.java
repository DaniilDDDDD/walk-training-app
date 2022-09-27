package web.fiiit.dataservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Data;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DataRepository extends ReactiveMongoRepository<Data, Long> {

    Mono<Data> findDataById(String id);

    Mono<Data> findDataByIdAndOwnerId(String id, Long ownerId);

    Flux<Data> findAllByOwnerId(Long id);

    Flux<Data> findDataByOwnerIdAndStartTimeAfterAndEndTimeBefore(
            Long ownerId,
            Date startTime,
            Date endTime
    );

    Flux<Data> findDataByOwnerIdAndStartTimeAndEndTime(
            Long id,
            Date startTime,
            Date endTime
    );

    Flux<Data> deleteDataByOwnerIdAndStartTimeAfterAndEndTimeBefore(
            Long id,
            Date startTime,
            Date endTime
    );

    Flux<Data> deleteAllByOwnerId(Long ownerId);

    Mono<Data> deleteDataByIdAndOwnerId(String dataId, Long ownerId);


}
