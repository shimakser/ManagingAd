package by.shimakser.controller.ad;

import by.shimakser.dto.AdvertiserDto;
import by.shimakser.mapper.AdvertiserMapper;
import by.shimakser.model.ad.Advertiser;
import by.shimakser.service.ad.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/advertisers")
public class AdvertiserController {

    private final AdvertiserService advertiserService;

    private final AdvertiserMapper advertiserMapper;

    @Autowired
    public AdvertiserController(AdvertiserService advertiserService, AdvertiserMapper advertiserMapper) {
        this.advertiserService = advertiserService;
        this.advertiserMapper = advertiserMapper;
    }

    @PostMapping
    public ResponseEntity<AdvertiserDto> addAdvertiser(@RequestBody AdvertiserDto advertiserDto)
            throws EntityExistsException {
        Advertiser newAdvertiser = advertiserMapper.mapToEntity(advertiserDto);
        Advertiser addedAdvertiser = advertiserService.add(newAdvertiser);
        return new ResponseEntity<>(advertiserMapper.mapToDto(addedAdvertiser), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public AdvertiserDto getAdvertiserById(@PathVariable Long id) {
        return advertiserMapper.mapToDto(advertiserService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AdvertiserDto> getAllAdvertisers(@RequestParam Optional<Integer> page,
                                                 @RequestParam Optional<Integer> size,
                                                 @RequestParam Optional<String> sortBy
    ) {
        return advertiserMapper.mapToListDto(advertiserService.getAll(page, size, sortBy));
    }

    @PutMapping(value = "/{id}")
    public AdvertiserDto updateAdvertiserById(@PathVariable("id") Long id, @RequestBody AdvertiserDto newAdvertiserDto) throws AuthenticationException {
        Advertiser advertiser = advertiserMapper.mapToEntity(newAdvertiserDto);
        Advertiser updatedAdvertiser = advertiserService.update(id, advertiser);
        return advertiserMapper.mapToDto(updatedAdvertiser);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteAdvertiserById(@PathVariable("id") Long id)
            throws AuthenticationException {
        advertiserService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deleted/{id}")
    public AdvertiserDto getDeletedAdvertiserById(@PathVariable Long id) {
        return advertiserMapper.mapToDto(advertiserService.getDeletedAdvertiser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deleted")
    public List<AdvertiserDto> getAllDeletedAdvertisers() {
        return advertiserMapper.mapToListDto(advertiserService.getDeletedAdvertisers());
    }
}
