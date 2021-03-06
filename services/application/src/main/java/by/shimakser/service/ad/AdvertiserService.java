package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.security.service.SecurityService;
import by.shimakser.model.ad.Advertiser;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.AdvertiserRepository;
import by.shimakser.repository.ad.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Slf4j
@Service
public class AdvertiserService {

    private final AdvertiserRepository advertiserRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final String DEFAULT_FIELD_SORT = "id";

    @Autowired
    public AdvertiserService(AdvertiserRepository advertiserRepository, UserRepository userRepository, SecurityService securityService) {
        this.advertiserRepository = advertiserRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Transactional(rollbackFor = {EntityExistsException.class, AuthorizationServiceException.class})
    public Advertiser add(Advertiser advertiser) throws EntityExistsException {
        boolean isAdvertiserByTitleExist = advertiserRepository
                .existsAdvertiserByAdvertiserTitle(advertiser.getAdvertiserTitle());

        if (isAdvertiserByTitleExist) {
            throw new EntityExistsException(ExceptionText.ALREADY_BOUND.getExceptionDescription());
        }

        String principalName = securityService.getPrincipalName();
        User principalUser = userRepository.findByUsername(principalName)
                .orElseThrow(() -> new AuthorizationServiceException(ExceptionText.AUTHORIZATION_SERVICE.getExceptionDescription()));
        advertiser.setCreator(principalUser);
        advertiserRepository.save(advertiser);

        log.info("Advertiser {} successfully created with title {}.", advertiser.getId(), advertiser.getAdvertiserTitle());
        return advertiser;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Advertiser get(Long id) throws EntityNotFoundException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        log.info("Search advertiser with id {}.", id);
        return Optional.of(advertiserById)
                .filter(not(Advertiser::isAdvertiserDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<Advertiser> getAll(Optional<Integer> page, Optional<Integer> size, Optional<String> sortBy) {
        log.info("Searched all advertisers by page {}, size {}, sort by {}.",
                page.orElseGet(() -> DEFAULT_PAGE), size.orElseGet(() -> DEFAULT_PAGE_SIZE), sortBy.orElseGet(() -> DEFAULT_FIELD_SORT));
        return advertiserRepository.findAllByAdvertiserDeletedFalse(
                PageRequest.of(page.orElse(DEFAULT_PAGE),
                        size.orElse(DEFAULT_PAGE_SIZE),
                        Sort.Direction.ASC, sortBy.orElse(DEFAULT_FIELD_SORT)));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public Advertiser update(Long id, Advertiser newAdvertiser) throws EntityNotFoundException, AuthenticationException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!securityService.checkPrincipalAccess(advertiserById.getCreator().getUsername())) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        newAdvertiser.setId(id);
        advertiserRepository.save(newAdvertiser);

        log.info("Advertiser {} updated.", id);
        return newAdvertiser;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public void delete(Long id) throws EntityNotFoundException, AuthenticationException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!securityService.checkPrincipalAccess(advertiserById.getCreator().getUsername())) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        advertiserById.setAdvertiserDeleted(true);
        advertiserRepository.save(advertiserById);

        log.info("Advertiser {} deleted.", id);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Advertiser getDeletedAdvertiser(Long id) throws EntityNotFoundException {
        log.info("Search deleted advertiser {}.", id);
        return advertiserRepository.findByIdAndAdvertiserDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<Advertiser> getDeletedAdvertisers() {
        log.info("Search all deleted advertisers.");
        return advertiserRepository.findAllByAdvertiserDeletedTrue();
    }
}
