package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.ad.Campaign;
import by.shimakser.model.ad.Role;
import by.shimakser.repository.ad.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Transactional(rollbackFor = AlreadyBoundException.class)
    public Campaign add(Campaign campaign) throws AlreadyBoundException {
        boolean isCampaignByTitleExist = campaignRepository
                .existsCampaignByCampaignTitle(campaign.getCampaignTitle());

        if (isCampaignByTitleExist) {
            throw new AlreadyBoundException(ExceptionText.ALREADY_BOUND.getExceptionDescription());
        }
        LocalDateTime date = LocalDateTime.now();
        campaign.setCampaignCreatedDate(date);
        campaignRepository.save(campaign);
        return campaign;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Campaign get(Long id) throws EntityNotFoundException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
        return Optional.of(campaignById)
                .filter(not(Campaign::isCampaignDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public Campaign update(Long id, Campaign newCampaign, JwtAuthenticationToken token) throws EntityNotFoundException, AuthenticationException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!checkPrincipalAccess(campaignById, token)) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        newCampaign.setId(id);
        campaignRepository.save(newCampaign);
        return newCampaign;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public void delete(Long id, JwtAuthenticationToken token) throws EntityNotFoundException, AuthenticationException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!checkPrincipalAccess(campaignById, token)) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        campaignById.setCampaignDeleted(true);
        LocalDateTime date = LocalDateTime.now();
        campaignById.setCampaignDeletedDate(date);
        campaignRepository.save(campaignById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Campaign getDeletedCampaign(Long id) throws EntityNotFoundException {
        return campaignRepository.findByIdAndCampaignDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<Campaign> getDeletedCampaigns() {
        return campaignRepository.findAllByCampaignDeletedTrue();
    }

    private boolean checkPrincipalAccess(Campaign campaign, JwtAuthenticationToken token) {
        String creatorName = campaign.getAdvertiser().getCreator().getUsername();

        String principalName = token.getTokenAttributes().get("preferred_username").toString();
        boolean isAdmin = token.getTokenAttributes().get("realm_access").toString().contains(Role.ADMIN.name());

        if (isAdmin || creatorName.equals(principalName)) {
            return true;
        }

        return false;
    }
}
