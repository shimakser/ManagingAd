package by.shimakser.filter.campaign;

import by.shimakser.filter.campaign.CampaignFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignRequest {

    private CampaignFilter campaignFilter;
    private Integer page;
    private Integer size;
    private String sortBy;
}
