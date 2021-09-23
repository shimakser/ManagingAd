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
        boolean addCheck = advertiserService.add(advertiser);
        if (!addCheck) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/advertiser/{id}")
    public ResponseEntity<List<Advertiser>> getAdvertiserById(@PathVariable Long id) {
        List<Advertiser> advertiser = advertiserService.get(id);
        if (advertiser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(advertiser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers")
    public ResponseEntity<List<Advertiser>> getAllAdvertisers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        List<Advertiser> advertisers = advertiserService.getAll(page, size, sortBy);
        return new ResponseEntity<>(advertisers, HttpStatus.OK);
    }

    @PutMapping(value = "/advertiser/{id}")
    public ResponseEntity<HttpStatus> updateAdvertiserById(@PathVariable("id") Long id, @RequestBody Advertiser advertiser, Principal creator) {
        boolean updatingCheck = advertiserService.update(id, advertiser, creator);
        if (!updatingCheck) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/advertiser/{id}")
    public ResponseEntity<HttpStatus> deleteAdvertiserById(@PathVariable("id") Long id, Principal creator) {
        boolean deleteCheck = advertiserService.delete(id, creator);
        if (!deleteCheck) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertiser/deleted/{id}")
    public ResponseEntity<Advertiser> getDeletedAdvertiserById(@PathVariable Long id) {
        Optional<Advertiser> advertiser = advertiserService.getDeletedAdvertiser(id);
        if (!advertiser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(advertiser.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers/deleted")
    public ResponseEntity<List<Advertiser>> getAllDeletedAdvertisers() {
        List<Advertiser> advertisers = advertiserService.getDeletedAdvertisers();
        return new ResponseEntity<>(advertisers, HttpStatus.OK);
    }
}
