package by.shimakser.mapper;

import by.shimakser.model.ad.Campaign;
import by.shimakser.model.tables.records.CampaignRecord;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CampaignRecordMapper {

    List<Campaign> mapToListEntity(List<CampaignRecord> campaignRecords);

    Campaign mapToEntity(CampaignRecord campaignRecord);
}
