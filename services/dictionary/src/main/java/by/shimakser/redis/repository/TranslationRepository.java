package by.shimakser.redis.repository;

import by.shimakser.redis.model.EntityTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<EntityTranslation, Long> {

    Optional<EntityTranslation> findEntityTranslationByEntityNameAndLanguage(String entity, String language);
}
