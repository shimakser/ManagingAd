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
        boolean addCheck = campaignService.add(campaign);
        if (!addCheck) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/campaign/{id}")
    public ResponseEntity<List<Campaign>> getCampaignById(@PathVariable Long id) {
        List<Campaign> campaign = campaignService.get(id);
        if (campaign.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campaign, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns")
    public ResponseEntity<List<Campaign>> getAllCampaigns(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        List<Campaign> campaigns = campaignService.getAll(page, size, sortBy);
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }

    @PutMapping(value = "/campaign/{id}")
    public ResponseEntity<HttpStatus> updateCampaignById(@PathVariable("id") Long id, @RequestBody Campaign campaign, Principal creator) {
        boolean updatingCheck = campaignService.update(id, campaign, creator);
        if (!updatingCheck) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/campaign/{id}")
    public ResponseEntity<HttpStatus> deleteCampaignById(@PathVariable("id") Long id, Principal creator) {
        boolean deleteCheck = campaignService.delete(id, creator);
        if (!deleteCheck) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaign/deleted/{id}")
    public ResponseEntity<Campaign> getDeletedCampaignById(@PathVariable Long id) {
        Optional<Campaign> campaign = campaignService.getDeletedCampaign(id);
        if (!campaign.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campaign.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns/deleted")
    public ResponseEntity<List<Campaign>> getAllDeletedCampaigns() {
        List<Campaign> campaigns = campaignService.getDeletedCampaigns();
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }
}
