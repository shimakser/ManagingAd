package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.security.service.SecurityService;
import by.shimakser.model.ad.Campaign;
import by.shimakser.repository.ad.CampaignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Slf4j
@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final SecurityService securityService;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, SecurityService securityService) {
        this.campaignRepository = campaignRepository;
        this.securityService = securityService;
    }

    @Transactional(rollbackFor = EntityExistsException.class)
    public Campaign add(Campaign campaign) throws EntityExistsException {
        boolean isCampaignByTitleExist = campaignRepository
                .existsCampaignByCampaignTitle(campaign.getCampaignTitle());

        if (isCampaignByTitleExist) {
            throw new EntityExistsException(ExceptionText.ALREADY_BOUND.getExceptionDescription());
        }
        LocalDateTime date = LocalDateTime.now();
        campaign.setCampaignCreatedDate(date);
        campaignRepository.save(campaign);

        log.info("Campaign {} successfully created with title {}.", campaign.getId(), campaign.getCampaignTitle());
        return campaign;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Campaign get(Long id) throws EntityNotFoundException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        log.info("Search campaign with id {}.", id);
        return Optional.of(campaignById)
                .filter(not(Campaign::isCampaignDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public Campaign update(Long id, Campaign newCampaign) throws EntityNotFoundException, AuthenticationException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!securityService.checkPrincipalAccess(campaignById.getAdvertiser().getCreator().getUsername())) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        newCampaign.setId(id);
        campaignRepository.save(newCampaign);

        log.info("Campaign {} updated.", id);
        return newCampaign;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public void delete(Long id) throws EntityNotFoundException, AuthenticationException {
        Campaign campaignById = campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!securityService.checkPrincipalAccess(campaignById.getAdvertiser().getCreator().getUsername())) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        campaignById.setCampaignDeleted(true);
        LocalDateTime date = LocalDateTime.now();
        campaignById.setCampaignDeletedDate(date);
        campaignRepository.save(campaignById);

        log.info("Campaign {} deleted.", id);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Campaign getDeletedCampaign(Long id) throws EntityNotFoundException {
        log.info("Search deleted campaign {}.", id);
        return campaignRepository.findByIdAndCampaignDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<Campaign> getDeletedCampaigns() {
        log.info("Search all deleted campaigns.");
        return campaignRepository.findAllByCampaignDeletedTrue();
    }
}
