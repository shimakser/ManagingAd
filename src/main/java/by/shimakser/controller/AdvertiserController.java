package by.shimakser.controller;

import by.shimakser.model.Advertiser;
import by.shimakser.service.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Advertiser getAdvertiserById(@PathVariable Long id) {
        return advertiserService.get(id);
    }

    @GetMapping(value = "/advertisers")
    public List<Advertiser> getAllAdvertisers() {
        return advertiserService.getAll();
    }

    @PutMapping(value = "/advertiser/{id}")
    public void updateAdvertiserById(@PathVariable("id") Long id, @RequestBody Advertiser advertiser) {
        advertiserService.update(id, advertiser);
    }

    @DeleteMapping(value = "/advertiser/{id}")
    public void deleteAdvertiserById(@PathVariable("id") Long id) {
        advertiserService.delete(id);
    }
}
