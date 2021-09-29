package by.shimakser.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="campaign")
public class Campaign {

    @Id
    private Long id;
    private String campaignTitle;
    private String campaignDescription;
    private String image;
    private String country;
    private String language;
    private String age;
    private String geolocation;
    @ManyToOne
    @JoinColumn(name = "advertiser_id")
    private Advertiser advertiser;

    private boolean campaignDeleted = Boolean.FALSE;
    private LocalDateTime campaignCreatedDate;
    private LocalDateTime campaignDeletedDate;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String countries) {
        this.country = countries;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String languages) {
        this.language = languages;
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

    public LocalDateTime getCampaignCreatedDate() {
        return campaignCreatedDate;
    }

    public void setCampaignCreatedDate(LocalDateTime campaignCreatedDate) {
        this.campaignCreatedDate = campaignCreatedDate;
    }

    public LocalDateTime getCampaignDeletedDate() {
        return campaignDeletedDate;
    }

    public void setCampaignDeletedDate(LocalDateTime campaignDeletedDate) {
        this.campaignDeletedDate = campaignDeletedDate;
    }
}
