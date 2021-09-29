package by.shimakser.mapper;

import by.shimakser.dto.CampaignDto;
import by.shimakser.model.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    @Mapping(target = "id", source = "campaign.id")
    @Mapping(target = "campaignTitle", source = "campaign.campaignTitle")
    @Mapping(target = "campaignDescription", source = "campaign.campaignDescription")
    @Mapping(target = "image", source = "campaign.image")
    @Mapping(target = "country", source = "campaign.country")
    @Mapping(target = "language", source = "campaign.language")
    @Mapping(target = "age", source = "campaign.age")
    @Mapping(target = "geolocation", source = "campaign.geolocation")
    @Mapping(target = "advertiser", source = "campaign.advertiser")
    CampaignDto mapToDto(Campaign campaign);


    @Mapping(target = "id", source = "campaignDto.id")
    @Mapping(target = "campaignTitle", source = "campaignDto.campaignTitle")
    @Mapping(target = "campaignDescription", source = "campaignDto.campaignDescription")
    @Mapping(target = "image", source = "campaignDto.image")
    @Mapping(target = "country", source = "campaignDto.country")
    @Mapping(target = "language", source = "campaignDto.language")
    @Mapping(target = "age", source = "campaignDto.age")
    @Mapping(target = "geolocation", source = "campaignDto.geolocation")
    @Mapping(target = "advertiser", source = "campaignDto.advertiser")
    Campaign mapToEntity(CampaignDto campaignDto);
}
