package by.shimakser.repository;

import by.shimakser.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, Long> {

    List<File> findAllByFileDeletedFalse();

    Optional<File> findFileByIdAndFileDeletedFalse(Long id);

    List<File> findAllByFileDeletedTrue();

    Optional<File> findFileByIdAndFileDeletedTrue(Long id);
}
