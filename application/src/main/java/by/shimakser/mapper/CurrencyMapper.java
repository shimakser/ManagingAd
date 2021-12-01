package by.shimakser.mapper;

import by.shimakser.dto.CurrencyDto;

import by.shimakser.model.currency.Currency;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    Currency mapToEntity(CurrencyDto currencyDto);

    List<Currency> mapToListEntity(List<CurrencyDto> currenciesDto);
}
