package by.shimakser.repository;

import by.shimakser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUserEmail(String userEmail);
    Optional<User> findByUsername(String username);
    Optional<User> findByIdAndUserDeletedTrue(Long id);
    List<User> findAllByUserDeletedTrue();
}
