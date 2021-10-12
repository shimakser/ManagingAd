package by.shimakser.controller.office;

import by.shimakser.dto.OfficeDto;
import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.mapper.OfficeMapper;
import by.shimakser.service.office.OfficeFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/offices")
public class CurrencyController {

    private final OfficeFilterService officeFilterService;

    private final OfficeMapper officeMapper;

    @Autowired
    public CurrencyController(OfficeFilterService officeFilterService, OfficeMapper officeMapper) {
        this.officeFilterService = officeFilterService;
        this.officeMapper = officeMapper;
    }

    @PostMapping
    public List<OfficeDto> getAllOffices(@RequestBody OfficeFilterRequest officeFilterRequest) {

        List<OfficeDto> list = new ArrayList<>();

        officeFilterService.getAllByConverter(officeFilterRequest).stream()
                .peek(response -> response.getOffice().setOfficePrice(response.getConvertedPrice()))
                .map(response -> officeMapper.mapToDto(response.getOffice()))
                .forEach(list::add);

        return list;
    }
}
