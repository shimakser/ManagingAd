package by.shimakser.model.office;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString(includeFieldNames = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    private Long id;
    private String contactPhoneNumber;
    private String contactEmail;
    private String contactSite;
}
