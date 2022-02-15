package by.shimakser.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CampaignFilterRequest extends PageRequest {

    private Set<String> age;

    private String country;

    private String campaignDeleteNotes;

    private String createdDateFrom;
    private String createdDateTo;

    public CampaignFilterRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }
}
