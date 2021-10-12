package by.shimakser.controller.office;

import by.shimakser.dto.OfficeDto;
import by.shimakser.filter.office.OfficeFilterRequest;
import by.shimakser.filter.office.OfficeFilterResponse;
import by.shimakser.mapper.OfficeMapper;
import by.shimakser.service.office.OfficeFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        List<OfficeFilterResponse> responseList = officeFilterService.getAllByFilter(officeFilterRequest);

        List<OfficeDto> list = new ArrayList<>();
        for (OfficeFilterResponse response: responseList) {
            OfficeDto oDto = officeMapper.mapToDto(response.getOffice());
            oDto.setConvertedPrice(response.getConvertedPrice());
            list.add(oDto);
        }
        return list;
    }
}
