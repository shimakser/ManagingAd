package by.shimakser.redis.config;

import by.shimakser.redis.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheInit {

    private final TranslationService translationService;

    @Autowired
    public RedisCacheInit(TranslationService translationService) {
        this.translationService = translationService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void redisCacheInitialize() {
        translationService.getTranslations()
                .forEach(entity -> translationService.getTranslationByCode(entity.getEntityName(), entity.getLanguage()));
    }
}
