package by.shimakser.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "office")
public class Office {

    @Id
    private Long id;
    private String officeTitle;
    private String officeAddress;
    @OneToMany
    @JoinColumn(name = "office_id")
    private List<Contact> officeContacts;
}
