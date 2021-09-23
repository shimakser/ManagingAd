package by.shimakser.service;

import by.shimakser.model.Campaign;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.CampaignRepository;
import by.shimakser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    public boolean add(Campaign campaign) {
        Optional<Campaign> campaignByTitle = campaignRepository
                .findByCampaignTitle(campaign.getCampaignTitle());

        if (campaignByTitle.isPresent()) {
            return false;
        }
        campaignRepository.save(campaign);
        return true;
    }

    public List<Campaign> get(Long id) {
        Optional<Campaign> campaignById = campaignRepository.findById(id);
        List<Campaign> campaign = Stream.of(campaignById.get()).filter(c -> c.isCampaignDeleted() == Boolean.FALSE).collect(Collectors.toList());
        return campaign;
    }

    public List<Campaign> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        List<Campaign> campaigns = campaignRepository.findAll(
                        PageRequest.of(page.orElse(0),
                                size.orElse(campaignRepository.findAll().size()),
                                Sort.Direction.ASC, sortBy.orElse("id")))
                .stream().filter(campaign -> campaign.isCampaignDeleted() == Boolean.FALSE).collect(Collectors.toList());
        return campaigns;
    }

    public boolean update(Long id, Campaign newCampaign, Principal creator) {
        if (!findCampaignByIdAndUserByPrincipal(id, creator)) {
            return false;
        }
        newCampaign.setId(id);
        campaignRepository.save(newCampaign);
        return true;
    }

    public boolean delete(Long id, Principal creator) {
        if (!findCampaignByIdAndUserByPrincipal(id, creator)) {
            return false;
        }
        campaignRepository.deleteById(id);
        return true;
    }

    public Optional<Campaign> getDeletedCampaign(Long id) {
        Optional<Campaign> deletedCampaignById = campaignRepository.findByIdAndCampaignDeletedTrue(id);
        return  deletedCampaignById;
    }

    public List<Campaign> getDeletedCampaigns() {
        List<Campaign> deletedAllCampaignsById = campaignRepository.findAllByCampaignDeletedTrue();
        return deletedAllCampaignsById;
    }

    public boolean findCampaignByIdAndUserByPrincipal(Long id, Principal user) {
        Optional<Campaign> campaignById = campaignRepository.findById(id);
        if (!campaignById.isPresent()) {
            return false;
        }
        User principalUser = userRepository.findByUsername(user.getName()).get();
        return (principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(campaignById.get().getAdvertiser().getCreator().getId()));
    }
}
