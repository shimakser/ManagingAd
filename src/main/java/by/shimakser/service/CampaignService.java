package by.shimakser.service;

import by.shimakser.model.Campaign;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.CampaignRepository;
import by.shimakser.repository.UserRepository;
import javassist.NotFoundException;
import netscape.security.ForbiddenTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
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

    @Transactional(rollbackFor = AlreadyBoundException.class)
    public Campaign add(Campaign campaign) throws AlreadyBoundException {
        boolean isCampaignByTitleExist = campaignRepository
                .existsCampaignByCampaignTitle(campaign.getCampaignTitle());

        if (isCampaignByTitleExist) {
            throw new AlreadyBoundException("Entered title is already taken.");
        }
        campaignRepository.save(campaign);
        return campaign;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public List<Campaign> get(Long id) throws NotFoundException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign is not found."));
        List<Campaign> campaign = Stream.of(campaignById)
                .filter(c -> c.isCampaignDeleted() == Boolean.FALSE)
                .collect(Collectors.toList());
        if (campaign.isEmpty()) {
            throw new NotFoundException("User is not found.");
        }
        return campaign;
    }

    @Transactional
    public List<Campaign> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        return campaignRepository.findAllByCampaignDeletedFalse(
                        PageRequest.of(page.orElse(0),
                                size.orElse(campaignRepository.findAllByCampaignDeletedFalse().size()),
                                Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Transactional(rollbackFor = {NotFoundException.class, ForbiddenTargetException.class})
    public Campaign update(Long id, Campaign newCampaign, Principal creator) throws NotFoundException {
        checkCampaignByIdAndUserByPrincipal(id, creator);
        newCampaign.setId(id);
        campaignRepository.save(newCampaign);
        return newCampaign;
    }

    @Transactional(rollbackFor = {NotFoundException.class, ForbiddenTargetException.class})
    public void delete(Long id, Principal creator) throws NotFoundException {
        checkCampaignByIdAndUserByPrincipal(id, creator);
        campaignRepository.deleteById(id);
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public Campaign getDeletedCampaign(Long id) throws NotFoundException {
        return campaignRepository.findByIdAndCampaignDeletedTrue(id)
                .orElseThrow(() -> new NotFoundException("Deleted campaign is not found."));
    }

    @Transactional
    public List<Campaign> getDeletedCampaigns() {
        return campaignRepository.findAllByCampaignDeletedTrue();
    }

    public void checkCampaignByIdAndUserByPrincipal(Long id, Principal user) throws NotFoundException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign is not found."));
        User principalUser = userRepository.findByUsername(user.getName()).get();
        boolean checkAccess = principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(campaignById.getAdvertiser().getCreator().getId());
        if (!checkAccess) {
            throw new ForbiddenTargetException("Insufficient rights to edit the advertiser.");
        }
    }
}
