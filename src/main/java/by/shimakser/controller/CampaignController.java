package by.shimakser.controller;

import by.shimakser.model.Campaign;
import by.shimakser.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping(value = "/campaign")
    public ResponseEntity<HttpStatus> addCampaign(@RequestBody Campaign campaign) {
        return campaignService.add(campaign);
    }

    @GetMapping(value = "/campaign/{id}")
    public ResponseEntity<List<Campaign>> getCampaignById(@PathVariable Long id) {
        return campaignService.get(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns")
    public ResponseEntity<List<Campaign>> getAllCampaigns(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return campaignService.getAll(page, size, sortBy);
    }

    @PutMapping(value = "/campaign/{id}")
    public ResponseEntity<HttpStatus> updateCampaignById(@PathVariable("id") Long id, @RequestBody Campaign campaign, Principal creator) {
        return campaignService.update(id, campaign, creator);
    }

    @DeleteMapping(value = "/campaign/{id}")
    public ResponseEntity<HttpStatus> deleteCampaignById(@PathVariable("id") Long id, Principal creator) {
        return campaignService.delete(id, creator);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaign/deleted/{id}")
    public ResponseEntity<Campaign> getDeletedCampaignById(@PathVariable Long id) {
        return campaignService.getDeletedCampaign(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns/deleted")
    public ResponseEntity<List<Campaign>> getAllDeletedCampaigns() {
        return campaignService.getDeletedCampaigns();
    }
}
