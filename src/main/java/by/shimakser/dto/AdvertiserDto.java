package by.shimakser.dto;

public class AdvertiserDto {

    private Long id;
    private String advertiserTitle;
    private String advertiserDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdvertiserTitle() {
        return advertiserTitle;
    }

    public void setAdvertiserTitle(String advertiserTitle) {
        this.advertiserTitle = advertiserTitle;
    }

    public String getAdvertiserDescription() {
        return advertiserDescription;
    }

    public void setAdvertiserDescription(String advertiserDescription) {
        this.advertiserDescription = advertiserDescription;
    }
}
