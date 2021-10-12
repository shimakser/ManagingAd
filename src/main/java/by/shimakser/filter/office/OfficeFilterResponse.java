package by.shimakser.filter.office;

import by.shimakser.model.office.Office;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeFilterResponse {

    private Double convertedPrice;
    private Office office;
}
