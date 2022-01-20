package by.shimakser.redis.service;

import by.shimakser.redis.model.EntityTranslation;
import by.shimakser.redis.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TranslationService {

    private final TranslationRepository repository;

    @Autowired
    public TranslationService(TranslationRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "translationCache", key="#entityName")
    public EntityTranslation getTranslationByCode(String entityName, String language) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return repository
                .findEntityTranslationByEntityNameAndLanguage(entityName, language)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<EntityTranslation> getTranslations() {
        return repository.findAll();
    }
}
