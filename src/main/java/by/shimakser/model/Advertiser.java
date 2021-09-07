package by.shimakser.model;

import javax.persistence.*;

@Entity
@Table(name="advertiser")
public class Advertiser {

    @Id
    private Long id;
    private String advertiserTitle;
    private String advertiserDescription;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    private boolean advertiserDeleted = Boolean.FALSE;

    public Advertiser() {}

    public Advertiser(String advertiserTitle, String advertiserDescription) {
        this.advertiserTitle = advertiserTitle;
        this.advertiserDescription = advertiserDescription;
    }

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public boolean isAdvertiserDeleted() {
        return advertiserDeleted;
    }

    public void setAdvertiserDeleted(boolean advertiserDeleted) {
        this.advertiserDeleted = advertiserDeleted;
    }
}
