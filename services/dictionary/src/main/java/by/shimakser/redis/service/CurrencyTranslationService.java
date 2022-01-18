package by.shimakser.redis.service;

import by.shimakser.redis.model.CurrencyTranslation;
import by.shimakser.redis.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CurrencyTranslationService {

    private final TranslationRepository repository;

    @Autowired
    public CurrencyTranslationService(TranslationRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "translationCache", key = "#code")
    public CurrencyTranslation getTranslationByCode(String code) {
        return repository
                .findAll()
                .stream().filter(translation -> translation.getCharCode().equals(code))
                .findAny().orElseThrow(NoSuchElementException::new);
    }
}
