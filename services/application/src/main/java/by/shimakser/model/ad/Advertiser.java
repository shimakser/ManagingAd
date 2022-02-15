package by.shimakser.model.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="advertiser")
@AllArgsConstructor
@NoArgsConstructor
public class Advertiser {

    @Id
    private Long id;
    private String advertiserTitle;
    private String advertiserDescription;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    private boolean advertiserDeleted = Boolean.FALSE;
}
