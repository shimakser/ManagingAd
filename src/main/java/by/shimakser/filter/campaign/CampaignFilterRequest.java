package by.shimakser.filter.campaign;

import by.shimakser.filter.Request;
import lombok.Data;

import java.util.Set;

@Data
public class CampaignFilter extends Request {

    private Set<String> age;

    private String country;

    private String campaignDeleteNotes;

    private String createdDateFrom;
    private String createdDateTo;

}
