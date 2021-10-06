package by.shimakser.mapper;

import by.shimakser.dto.AdvertiserDto;
import by.shimakser.model.Advertiser;
import by.shimakser.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdvertiserMapper {

    AdvertiserDto mapToDto(Advertiser advertiser);

    List<AdvertiserDto> mapToListDto(List<Advertiser> advertisers);

    Advertiser mapToEntity(AdvertiserDto advertiserDto);
}
