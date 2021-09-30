package by.shimakser.service;

import by.shimakser.model.Campaign;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.CampaignRepository;
import by.shimakser.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.rmi.AlreadyBoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

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
        LocalDateTime date = LocalDateTime.now();
        campaign.setCampaignCreatedDate(date);
        campaignRepository.save(campaign);
        return campaign;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public Campaign get(Long id) throws NotFoundException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign is not found."));
        return Optional.of(campaignById)
                .filter(not(Campaign::isCampaignDeleted))
                .orElseThrow(() -> new NotFoundException("Campaign is not found."));
    }

    @Transactional(rollbackFor = {NotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public Campaign update(Long id, Campaign newCampaign, Principal creator) throws NotFoundException, AuthenticationException {
        checkCampaignByIdAndUserByPrincipal(id, creator);
        newCampaign.setId(id);
        campaignRepository.save(newCampaign);
        return newCampaign;
    }

    @Transactional(rollbackFor = {NotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public void delete(Long id, Principal creator) throws NotFoundException, AuthenticationException {
        Campaign campaignById = checkCampaignByIdAndUserByPrincipal(id, creator);
        campaignById.setCampaignDeleted(true);
        LocalDateTime date = LocalDateTime.now();
        campaignById.setCampaignDeletedDate(date);
        campaignRepository.save(campaignById);
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

    public Campaign checkCampaignByIdAndUserByPrincipal(Long id, Principal user)
            throws NotFoundException, AuthenticationException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign is not found."));
        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException("Not authorized."));
        boolean checkAccess = principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(campaignById.getAdvertiser().getCreator().getId());
        if (!checkAccess) {
            throw new AuthenticationException("Insufficient rights to edit the advertiser.");
        }
        return campaignById;
    }
}
