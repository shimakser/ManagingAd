package by.shimakser.model.office;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private String id;
    private String numCode;
    private String charCode;
    private String nominal;
    private String name;
    private String value;
    private String previous;
}
