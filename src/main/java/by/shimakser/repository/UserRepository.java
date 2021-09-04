package by.shimakser.repository;

import by.shimakser.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUsername(String username);
}
