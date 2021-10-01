package by.shimakser.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    private Long id;
    private String contactPhoneNumber;
    private String contactEmail;
    private String contactSite;
}
