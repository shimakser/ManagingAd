package by.shimakser.controller;

import by.shimakser.model.Campaign;
import by.shimakser.model.User;
import by.shimakser.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void addCampaign(@RequestBody Campaign campaign) {
        campaignService.add(campaign);
    }

    @GetMapping(value = "/campaign/{id}")
    public List<Optional<Campaign>> getCampaignById(@PathVariable Long id) {
        return campaignService.get(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns")
    public List<Campaign> getAllCampaigns(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return campaignService.getAll(page, size, sortBy);
    }

    @PutMapping(value = "/campaign/{id}")
    public void updateCampaignById(@PathVariable("id") Long id, @RequestBody Campaign campaign) {
        campaignService.update(id, campaign);
    }

    @DeleteMapping(value = "/campaign/{id}")
    public void deleteCampaignById(@PathVariable("id") Long id) {
        campaignService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaign/deleted/{id}")
    public Campaign getDeletedCampaignById(@PathVariable Long id) {
        return campaignService.getDeletedCampaign(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/campaigns/deleted")
    public List<Campaign> getAllDeletedCampaigns() {
        return campaignService.getDeletedCampaigns();
    }
}
