package by.shimakser.redis.controller;

import by.shimakser.redis.model.EntityTranslation;
import by.shimakser.dto.TranslationRequest;
import by.shimakser.redis.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translation")
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public EntityTranslation getTranslation(@RequestBody TranslationRequest request) {
        return translationService.getTranslationByCode(request.getEntityName(), request.getLanguage());
    }
}
