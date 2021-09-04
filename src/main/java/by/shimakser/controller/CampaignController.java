package by.shimakser.controller;

import by.shimakser.model.Campaign;
import by.shimakser.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Campaign getCampaignById(@PathVariable Long id) {
        return campaignService.get(id);
    }

    @GetMapping(value = "/campaigns")
    public List<Campaign> getAllCampaigns() {
        return campaignService.getAll();
    }

    @PutMapping(value = "/campaign/{id}")
    public void updateCampaignById(@PathVariable("id") Long id, @RequestBody Campaign campaign) {
        campaignService.update(id, campaign);
    }

    @DeleteMapping(value = "/campaign/{id}")
    public void deleteCampaignById(@PathVariable("id") Long id) {
        campaignService.delete(id);
    }
}
