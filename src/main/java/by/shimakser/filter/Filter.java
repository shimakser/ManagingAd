package by.shimakser.filter;

import java.util.Set;

public class Filter {

    private Set<String> age;

    private String country;

    private String campaignDeleteNotes;


    private String createdDateFrom;
    private String createdDateTo;

    public String getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(String createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public String getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(String createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public String getCampaignDeleteNotes() {
        return campaignDeleteNotes;
    }

    public void setCampaignDeleteNotes(String campaignDeleteNotes) {
        this.campaignDeleteNotes = campaignDeleteNotes;
    }

    public Set<String> getAge() {
        return age;
    }

    public void setAge(Set<String> age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
