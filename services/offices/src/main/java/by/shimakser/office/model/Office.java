package by.shimakser.office.model;

import by.shimakser.office.annotation.ExportField;
import by.shimakser.office.converter.ListConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(includeFieldNames = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Office {

    @Id
    @CsvBindByName(column = "id")
    private Long id;

    @ExportField(name = "title")
    @CsvBindByName(column = "office_title")
    private String officeTitle;

    @ExportField(name = "address")
    @CsvBindByName(column = "office_address")
    private String officeAddress;

    @ExportField(name = "price")
    @CsvBindByName(column = "office_price")
    private Double officePrice;

    @ExportField(name = "contacts")
    @CsvCustomBindByName(column = "office_contacts", converter = ListConverter.class)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "office_id")
    private List<Contact> officeContacts;

    @ExportField(name = "description")
    @Type(type = "jsonb")
    @Column(name = "office_description", columnDefinition = "jsonb")
    private String officeDescription;
}
