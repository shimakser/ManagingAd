package by.shimakser.model;

import javax.persistence.*;

@Entity
@Table(name="campaign")
public class Campaign {

    @Id
    private Long id;
    private String campaignTitle;
    private String campaignDescription;
    private String image;
    private String countries;
    private String languages;
    private String age;
    private String geolocation;
    @ManyToOne
    @JoinColumn(name = "advertiser_id")
    private Advertiser advertiser;
    private boolean campaignDeleted = Boolean.FALSE;

    public Campaign() {}

    public Campaign(String campaignTitle, String campaignDescription, String image, String countries, String languages, String age, String geolocation) {
        this.campaignTitle = campaignTitle;
        this.campaignDescription = campaignDescription;
        this.image = image;
        this.countries = countries;
        this.languages = languages;
        this.age = age;
        this.geolocation = geolocation;
    }

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

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    public boolean isCampaignDeleted() {
        return campaignDeleted;
    }

    public void setCampaignDeleted(boolean campaignDeleted) {
        this.campaignDeleted = campaignDeleted;
    }
}
