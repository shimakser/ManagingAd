package by.shimakser.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

@Getter
@Setter
public class CampaignFilterRequest extends PageRequest {

    private Set<String> age;

    private String country;

    private String campaignDeleteNotes;

    private String createdDateFrom;
    private String createdDateTo;

    public CampaignFilterRequest(int page, int size, Sort sort,
                                 Set<String> age, String country, String campaignDeleteNotes,
                                 String createdDateFrom, String createdDateTo) {
        super(page, size, sort);
        this.age = age;
        this.country = country;
        this.campaignDeleteNotes = campaignDeleteNotes;
        this.createdDateFrom = createdDateFrom;
        this.createdDateTo = createdDateTo;
    }
}
