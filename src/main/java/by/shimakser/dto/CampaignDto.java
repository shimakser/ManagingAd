package by.shimakser.dto;

import by.shimakser.model.Advertiser;

public class CampaignDto {

    private Long id;
    private String campaignTitle;
    private String campaignDescription;
    private String image;
    private String countries;
    private String languages;
    private String age;
    private String geolocation;
    private AdvertiserDto advertiser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public AdvertiserDto getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(AdvertiserDto advertiser) {
        this.advertiser = advertiser;
    }
}
