package by.shimakser.mapper;

import by.shimakser.dto.AdvertiserDto;
import by.shimakser.model.Advertiser;
import by.shimakser.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = User.class)
public interface AdvertiserMapper {

    @Mapping(target = "id", source = "advertiser.id")
    @Mapping(target = "advertiserTitle", source = "advertiser.advertiserTitle")
    @Mapping(target = "advertiserDescription", source = "advertiser.advertiserDescription")
    AdvertiserDto mapToDto(Advertiser advertiser);


    @Mapping(target = "id", source = "advertiserDto.id")
    @Mapping(target = "advertiserTitle", source = "advertiserDto.advertiserTitle")
    @Mapping(target = "advertiserDescription", source = "advertiserDto.advertiserDescription")
    Advertiser mapToEntity(AdvertiserDto advertiserDto);
}
