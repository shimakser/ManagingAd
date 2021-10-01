package by.shimakser.filter;

import lombok.Data;

import java.util.Set;

@Data
public class Filter {

    private Set<String> age;

    private String country;

    private String campaignDeleteNotes;

    private String createdDateFrom;
    private String createdDateTo;

}
