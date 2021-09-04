package by.shimakser.service;

import by.shimakser.model.Campaign;
import by.shimakser.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public void add(Campaign campaign) {
        Campaign campaignByTitle = campaignRepository.findByCampaignTitle(campaign.getCampaignTitle());

        if (campaignByTitle == null) {
            campaignRepository.save(campaign);
        }
    }

    public Campaign get(Long id) {
        Optional<Campaign> campaignById = campaignRepository.findById(id);
        return campaignById.get();
    }

    public List<Campaign> getAll() {
        List<Campaign> allCampaigns = (List<Campaign>) campaignRepository.findAll();
        return allCampaigns;
    }

    public void update(Long id, Campaign newCampaign) {
        Optional<Campaign> campaignById = campaignRepository.findById(id);
        if (campaignById.isPresent()) {
            newCampaign.setId(id);
            campaignRepository.save(newCampaign);
        }
    }

    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }
}
