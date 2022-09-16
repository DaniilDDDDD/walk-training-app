package web.fiiit.dataservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.fiiit.dataservice.document.Data;

import java.util.Date;

@Repository
public interface DataRepository extends ReactiveMongoRepository<Data, Long> {

    Mono<Data> findDataById(Long id);

    Flux<Data> findAllByOwnerId(Long id);

    Flux<Data> findDataByOwnerIdAndBeforeAfterAndAfterBefore(
            Long id,
            Date before,
            Date after
    );

}
