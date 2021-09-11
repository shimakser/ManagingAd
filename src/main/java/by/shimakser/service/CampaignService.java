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

    public ResponseEntity<HttpStatus> add(Campaign campaign) {
        Optional<Campaign> campaignByTitle = campaignRepository
                .findByCampaignTitle(campaign.getCampaignTitle());

        if (campaignByTitle.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        campaignRepository.save(campaign);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<List<Campaign>> get(Long id) {
        Optional<Campaign> campaignById = campaignRepository.findById(id);
        List<Campaign> campaigns = Stream.of(campaignById.get()).filter(c -> c.isCampaignDeleted() == Boolean.FALSE).collect(Collectors.toList());
        if (campaigns.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }

    public ResponseEntity<List<Campaign>> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        List<Campaign> campaigns = campaignRepository.findAll(
                        PageRequest.of(page.orElse(0),
                                size.orElse(campaignRepository.findAll().size()),
                                Sort.Direction.ASC, sortBy.orElse("id")))
                .stream().filter(campaign -> campaign.isCampaignDeleted() == Boolean.FALSE).collect(Collectors.toList());
        if (campaigns.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> update(Long id, Campaign newCampaign, Principal creator) {
        if (findCampaignByIdAndUserByPrincipal(id, creator)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newCampaign.setId(id);
        campaignRepository.save(newCampaign);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> delete(Long id, Principal creator) {
        if (findCampaignByIdAndUserByPrincipal(id, creator)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        campaignRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Campaign> getDeletedCampaign(Long id) {
        Optional<Campaign> deletedCampaignById = campaignRepository.findByIdAndCampaignDeletedTrue(id);
        if (!deletedCampaignById.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deletedCampaignById.get(), HttpStatus.OK);
    }

    public ResponseEntity<List<Campaign>> getDeletedCampaigns() {
        List<Campaign> deletedAllCampaignsById = campaignRepository.findAllByCampaignDeletedTrue();
        if (deletedAllCampaignsById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deletedAllCampaignsById, HttpStatus.OK);
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
