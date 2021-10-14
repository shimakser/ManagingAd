package by.shimakser.dto;

import lombok.Data;

@Data
public class CampaignDto {

    private Long id;
    private String campaignTitle;
    private String campaignDescription;
    private String image;
    private String country;
    private String language;
    private String age;
    private String geolocation;
    private AdvertiserDto advertiser;
}
