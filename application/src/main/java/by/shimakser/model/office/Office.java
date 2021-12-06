package by.shimakser.model.office;

import by.shimakser.converter.JSONObjectConverter;
import by.shimakser.converter.JsonConverter;
import by.shimakser.converter.ListConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
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
    @CsvBindByName(column = "id")
    private Long id;

    @CsvBindByName(column = "office_title")
    private String officeTitle;

    @CsvBindByName(column = "office_address")
    private String officeAddress;

    @CsvBindByName(column = "office_price")
    private Double officePrice;

    @CsvCustomBindByName(column = "office_contacts", converter = ListConverter.class)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "office_id")
    private List<Contact> officeContacts;

    @CsvCustomBindByName(column = "office_description", converter = JsonConverter.class)
    @Convert(converter = JSONObjectConverter.class)
    private JSONObject officeDescription;

    @Override
    public String toString() {
        return id + "," + officeTitle + "," + officeAddress + "," + officePrice + "," + officeContacts + "," + officeDescription;
    }
}
