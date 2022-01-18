package by.shimakser.redis.controller;

import by.shimakser.redis.model.CurrencyTranslation;
import by.shimakser.redis.service.CurrencyTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency/translation")
public class CurrencyTranslationController {

    private final CurrencyTranslationService translationService;

    @Autowired
    public CurrencyTranslationController(CurrencyTranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping(value = "/{code}")
    public CurrencyTranslation getTranslation(@PathVariable String code) {
        return translationService.getTranslationByCode(code);
    }
}
