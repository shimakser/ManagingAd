package by.shimakser.currencies.mapper;

import by.shimakser.currencies.model.Currency;
import by.shimakser.dto.CurrencyDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    Currency mapToEntity(CurrencyDto currencyDto);

    List<Currency> mapToListEntity(List<CurrencyDto> currenciesDto);
}
