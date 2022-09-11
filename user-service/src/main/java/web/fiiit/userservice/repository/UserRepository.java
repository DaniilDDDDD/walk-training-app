package web.fiiit.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.fiiit.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByFirstNameOrLastName(String firstName, String lastName);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    List<User> findAllByDoctor(User doctor);
    void deleteUserByUsername(String username);

}
