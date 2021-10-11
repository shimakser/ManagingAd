package by.shimakser.model.office;

import by.shimakser.converter.JSONObjectConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office")
public class Office {

    @Id
    private Long id;
    private String officeTitle;
    private String officeAddress;
    private Double officePrice;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "office_id")
    private List<Contact> officeContacts;
    @Convert(converter = JSONObjectConverter.class)
    private JSONObject officeDescription;

    @Override
    public String toString() {
        return id + "," + officeTitle + "," + officeAddress + "," + officePrice + "," + officeContacts + "," + officeDescription;
    }
}
