package by.shimakser.service;

import by.shimakser.model.Campaign;
import by.shimakser.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public void add(Campaign campaign) {
        Optional<Campaign> campaignByTitle = Optional.ofNullable(campaignRepository
                .findByCampaignTitle(campaign.getCampaignTitle()));

        if (!campaignByTitle.isPresent()) {
            campaignRepository.save(campaign);
        }
    }

    public List<Optional<Campaign>> get(Long id) {
        Optional<Campaign> campaignById = campaignRepository.findById(id);
        return Stream.of(campaignById).filter(c -> c.get().isCampaignDeleted() == Boolean.FALSE).collect(Collectors.toList());
    }

    public List<Campaign> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        return campaignRepository.findAll(
                PageRequest.of(page.orElse(0),
                        size.orElse(campaignRepository.findAll().size()),
                        Sort.Direction.ASC, sortBy.orElse("id")))
                        .stream().filter(campaign -> campaign.isCampaignDeleted() == Boolean.FALSE).collect(Collectors.toList());
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

    public Campaign getDeletedCampaign(Long id) {
        Optional<Campaign> deletedCampaignById = Optional.of(campaignRepository
                .findByIdAndCampaignDeletedTrue(id));
        return deletedCampaignById.get();
    }

    public List<Campaign> getDeletedCampaigns() {
        List<Campaign> deletedAllCampaignsById = campaignRepository.findAllByCampaignDeletedTrue();
        return deletedAllCampaignsById;
    }
}
