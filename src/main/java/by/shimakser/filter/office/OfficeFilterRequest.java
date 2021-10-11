package by.shimakser.filter.office;

import by.shimakser.filter.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeFilterRequest extends Request {

    private Currencies currency;
}
