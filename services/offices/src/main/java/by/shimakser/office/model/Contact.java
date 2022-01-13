package by.shimakser.office.model;

import by.shimakser.office.annotation.ExportField;
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
    @ExportField
    private Long id;

    @ExportField(name = "phone")
    private String contactPhoneNumber;

    @ExportField(name = "email")
    private String contactEmail;

    @ExportField(name = "site")
    private String contactSite;
}
