package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
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
import javax.persistence.EntityNotFoundException;
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
            throw new AlreadyBoundException(ExceptionText.AlreadyBound.getExceptionText());
        }
        LocalDateTime date = LocalDateTime.now();
        campaign.setCampaignCreatedDate(date);
        campaignRepository.save(campaign);
        return campaign;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Campaign get(Long id) throws EntityNotFoundException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
        return Optional.of(campaignById)
                .filter(not(Campaign::isCampaignDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public Campaign update(Long id, Campaign newCampaign, Principal creator) throws EntityNotFoundException, AuthenticationException {
        checkCampaignByIdAndUserByPrincipal(id, creator);
        newCampaign.setId(id);
        campaignRepository.save(newCampaign);
        return newCampaign;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public void delete(Long id, Principal creator) throws EntityNotFoundException, AuthenticationException {
        Campaign campaignById = checkCampaignByIdAndUserByPrincipal(id, creator);
        campaignById.setCampaignDeleted(true);
        LocalDateTime date = LocalDateTime.now();
        campaignById.setCampaignDeletedDate(date);
        campaignRepository.save(campaignById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Campaign getDeletedCampaign(Long id) throws EntityNotFoundException {
        return campaignRepository.findByIdAndCampaignDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional
    public List<Campaign> getDeletedCampaigns() {
        return campaignRepository.findAllByCampaignDeletedTrue();
    }

    public Campaign checkCampaignByIdAndUserByPrincipal(Long id, Principal user)
            throws EntityNotFoundException, AuthenticationException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException("Not authorized."));
        boolean checkAccess = principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(campaignById.getAdvertiser().getCreator().getId());
        if (!checkAccess) {
            throw new AuthenticationException(ExceptionText.Authentication.getExceptionText());
        }
        return campaignById;
    }
}
