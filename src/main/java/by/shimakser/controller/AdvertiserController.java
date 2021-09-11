package by.shimakser.controller;

import by.shimakser.model.Advertiser;
import by.shimakser.service.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class AdvertiserController {

    private final AdvertiserService advertiserService;

    @Autowired
    public AdvertiserController(AdvertiserService advertiserService) {
        this.advertiserService = advertiserService;
    }

    @PostMapping(value = "/advertiser")
    public ResponseEntity<HttpStatus> addAdvertiser(@RequestBody Advertiser advertiser) {
        return advertiserService.add(advertiser);
    }

    @GetMapping(value = "/advertiser/{id}")
    public ResponseEntity<List<Advertiser>> getAdvertiserById(@PathVariable Long id) {
        return advertiserService.get(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers")
    public ResponseEntity<List<Advertiser>> getAllAdvertisers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return advertiserService.getAll(page, size, sortBy);
    }

    @PutMapping(value = "/advertiser/{id}")
    public ResponseEntity<HttpStatus> updateAdvertiserById(@PathVariable("id") Long id, @RequestBody Advertiser advertiser, Principal creator) {
        return advertiserService.update(id, advertiser, creator);
    }

    @DeleteMapping(value = "/advertiser/{id}")
    public ResponseEntity<HttpStatus> deleteAdvertiserById(@PathVariable("id") Long id, Principal creator) {
        return advertiserService.delete(id, creator);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertiser/deleted/{id}")
    public ResponseEntity<Advertiser> getDeletedAdvertiserById(@PathVariable Long id) {
        return advertiserService.getDeletedAdvertiser(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers/deleted")
    public ResponseEntity<List<Advertiser>> getAllDeletedAdvertisers() {
        return advertiserService.getDeletedAdvertisers();
    }
}
