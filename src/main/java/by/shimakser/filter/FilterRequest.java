package by.shimakser.filter;

import lombok.Data;

@Data
public class FilterRequest {

    private CampaignFilter campaignFilter;
    private Integer page;
    private Integer size;
    private String sortBy;

    public FilterRequest() {}

    public FilterRequest(CampaignFilter campaignFilter, Integer page, Integer size, String sortBy) {
        this.campaignFilter = campaignFilter;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
    }
}
