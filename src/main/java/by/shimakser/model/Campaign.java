package by.shimakser.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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
    private String campaignDeleteNotes;
}
