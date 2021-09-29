package by.shimakser.controller;

import by.shimakser.dto.AdvertiserDto;
import by.shimakser.mapper.AdvertiserMapper;
import by.shimakser.model.Advertiser;
import by.shimakser.service.AdvertiserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.rmi.AlreadyBoundException;
import java.security.Principal;
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
    public ResponseEntity<AdvertiserDto> addAdvertiser(@RequestBody AdvertiserDto advertiserDto, Principal user)
            throws AlreadyBoundException {
        Advertiser newAdvertiser = advertiserMapper.mapToEntity(advertiserDto);
        advertiserService.add(newAdvertiser, user);
        return new ResponseEntity<>(advertiserDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public AdvertiserDto getAdvertiserById(@PathVariable Long id) throws NotFoundException {
        return advertiserMapper.mapToDto(advertiserService.get(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<AdvertiserDto> getAllAdvertisers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return advertiserMapper.mapToListDto(advertiserService.getAll(page, size, sortBy));
    }

    @PutMapping(value = "/{id}")
    public AdvertiserDto updateAdvertiserById(@PathVariable("id") Long id,
                                              @RequestBody AdvertiserDto newAdvertiserDto,
                                              Principal creator) throws NotFoundException {
        Advertiser advertiser = advertiserMapper.mapToEntity(newAdvertiserDto);
        advertiserService.update(id, advertiser, creator);
        return newAdvertiserDto;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteAdvertiserById(@PathVariable("id") Long id, Principal creator)
            throws NotFoundException {
        advertiserService.delete(id, creator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/deleted/{id}")
    public AdvertiserDto getDeletedAdvertiserById(@PathVariable Long id) throws NotFoundException {
        return advertiserMapper.mapToDto(advertiserService.getDeletedAdvertiser(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/deleted")
    public List<AdvertiserDto> getAllDeletedAdvertisers() {
        return advertiserMapper.mapToListDto(advertiserService.getDeletedAdvertisers());
    }
}
