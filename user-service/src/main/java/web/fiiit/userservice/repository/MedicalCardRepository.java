package web.fiiit.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.fiiit.userservice.model.MedicalCard;
import web.fiiit.userservice.model.User;

import java.util.List;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, Long> {

    List<MedicalCard> findAllByOwner(User owner);

}
