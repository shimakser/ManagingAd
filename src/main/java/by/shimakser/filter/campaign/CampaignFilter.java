package by.shimakser.filter.campaign;

import lombok.Data;

import java.util.Set;

@Data
public class CampaignFilter {

    private Set<String> age;

    private String country;

    private String campaignDeleteNotes;

    private String createdDateFrom;
    private String createdDateTo;

}
