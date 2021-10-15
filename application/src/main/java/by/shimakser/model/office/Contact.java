package by.shimakser.model.office;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
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

    @Override
    public String toString() {
        return id + "," + contactPhoneNumber + "," + contactEmail + "," + contactSite;
    }
}
