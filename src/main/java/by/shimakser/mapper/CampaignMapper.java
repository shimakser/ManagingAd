package by.shimakser.mapper;

import by.shimakser.dto.CampaignDto;
import by.shimakser.model.Campaign;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    CampaignDto mapToDto(Campaign campaign);

    List<CampaignDto> mapToListDto(List<Campaign> campaigns);

    Campaign mapToEntity(CampaignDto campaignDto);
}
