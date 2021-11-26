package by.shimakser.currencies.service;

import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.model.CurrencyCode;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConvertService {

    public List<Currency> getListEntity(JSONObject json) {
        Map<String, Map<String, JSONObject>> mapOfCurrencies = (LinkedHashMap) json.get("Valute");

        List<Currency> listOfCurrency = new ArrayList<>();
        Arrays.stream(CurrencyCode.values())
                .map(code -> mapOfCurrencies.get(code.toString()))
                .map(Map::values)
                .map(this::convertToEntity)
                .forEach(listOfCurrency::add);
        return listOfCurrency;
    }

    public Currency getEntity(JSONObject json, String id) {
        Map<String, Map<String, JSONObject>> mapOfCurrencies = (LinkedHashMap) json.get("Valute");
        return mapOfCurrencies.values()
                .stream()
                .filter(stringJom -> stringJom.containsValue(id))
                .map(Map::values)
                .map(this::convertToEntity)
                .collect(Collectors.toList())
                .get(0);
    }

    public Currency convertToEntity(Collection<JSONObject> values) {
        String[] value = values.toString().substring(1, values.toString().length() - 1).split(", ");

        return new Currency(value[0], value[1], value[2], value[3], value[4], value[5], value[6]);
    }
}
