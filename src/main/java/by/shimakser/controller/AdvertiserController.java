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
import java.util.stream.Collectors;

@RestController
public class AdvertiserController {

    private final AdvertiserService advertiserService;

    private final AdvertiserMapper advertiserMapper;

    @Autowired
    public AdvertiserController(AdvertiserService advertiserService, AdvertiserMapper advertiserMapper) {
        this.advertiserService = advertiserService;
        this.advertiserMapper = advertiserMapper;
    }

    @PostMapping(value = "/advertisers")
    public ResponseEntity<AdvertiserDto> addAdvertiser(@RequestBody AdvertiserDto advertiserDto, Principal user)
            throws AlreadyBoundException {
        Advertiser newAdvertiser = advertiserMapper.mapToEntity(advertiserDto);
        advertiserService.add(newAdvertiser, user);
        return new ResponseEntity<>(advertiserDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/advertisers/{id}")
    public List<AdvertiserDto> getAdvertiserById(@PathVariable Long id) throws NotFoundException {
        return advertiserService.get(id)
                .stream().map(advertiserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers")
    public List<AdvertiserDto> getAllAdvertisers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return advertiserService.getAll(page, size, sortBy)
                .stream().map(advertiserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/advertisers/{id}")
    public AdvertiserDto updateAdvertiserById(@PathVariable("id") Long id,
                                              @RequestBody AdvertiserDto newAdvertiserDto,
                                              Principal creator) throws NotFoundException {
        Advertiser advertiser = advertiserMapper.mapToEntity(newAdvertiserDto);
        advertiserService.update(id, advertiser, creator);
        return newAdvertiserDto;
    }

    @DeleteMapping(value = "/advertisers/{id}")
    public ResponseEntity<HttpStatus> deleteAdvertiserById(@PathVariable("id") Long id, Principal creator)
            throws NotFoundException {
        advertiserService.delete(id, creator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers/deleted/{id}")
    public AdvertiserDto getDeletedAdvertiserById(@PathVariable Long id) throws NotFoundException {
        return advertiserMapper.mapToDto(advertiserService.getDeletedAdvertiser(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers/deleted")
    public List<AdvertiserDto> getAllDeletedAdvertisers() {
        return advertiserService.getDeletedAdvertisers()
                .stream().map(advertiserMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
