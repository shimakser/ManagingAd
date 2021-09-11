package by.shimakser.controller;

import by.shimakser.model.Advertiser;
import by.shimakser.model.User;
import by.shimakser.service.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void addAdvertiser(@RequestBody Advertiser advertiser) {
        advertiserService.add(advertiser);
    }

    @GetMapping(value = "/advertiser/{id}")
    public List<Optional<Advertiser>> getAdvertiserById(@PathVariable Long id) {
        return advertiserService.get(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers")
    public Page<Advertiser> getAllAdvertisers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return advertiserService.getAll(page, size, sortBy);
    }

    @PutMapping(value = "/advertiser/{id}")
    public void updateAdvertiserById(@PathVariable("id") Long id, @RequestBody Advertiser advertiser) {
        advertiserService.update(id, advertiser);
    }

    @DeleteMapping(value = "/advertiser/{id}")
    public void deleteAdvertiserById(@PathVariable("id") Long id) {
        advertiserService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertiser/deleted/{id}")
    public Advertiser getDeletedAdvertiserById(@PathVariable Long id) {
        return advertiserService.getDeletedAdvertiser(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/advertisers/deleted")
    public List<Advertiser> getAllDeletedAdvertisers() {
        return advertiserService.getDeletedAdvertisers();
    }
}
