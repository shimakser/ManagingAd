package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.ad.Advertiser;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.AdvertiserRepository;
import by.shimakser.repository.ad.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
public class AdvertiserService {

    private final AdvertiserRepository advertiserRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdvertiserService(AdvertiserRepository advertiserRepository, UserRepository userRepository) {
        this.advertiserRepository = advertiserRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = {AlreadyBoundException.class, AuthorizationServiceException.class})
    public Advertiser add(Advertiser advertiser, JwtAuthenticationToken token) throws AlreadyBoundException {
        boolean isAdvertiserByTitleExist = advertiserRepository
                .existsAdvertiserByAdvertiserTitle(advertiser.getAdvertiserTitle());

        if (isAdvertiserByTitleExist) {
            throw new AlreadyBoundException(ExceptionText.ALREADY_BOUND.getExceptionDescription());
        }

        String principalName = token.getTokenAttributes().get("preferred_username").toString();
        User principalUser = userRepository.findByUsername(principalName)
                .orElseThrow(() -> new AuthorizationServiceException(ExceptionText.AUTHORIZATION_SERVICE.getExceptionDescription()));
        advertiser.setCreator(principalUser);
        advertiserRepository.save(advertiser);
        return advertiser;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Advertiser get(Long id) throws EntityNotFoundException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
        return Optional.of(advertiserById)
                .filter(not(Advertiser::isAdvertiserDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<Advertiser> getAll(Optional<Integer> page, Optional<Integer> size, Optional<String> sortBy) {
        return advertiserRepository.findAllByAdvertiserDeletedFalse(
                PageRequest.of(page.orElse(0),
                        size.orElse(10),
                        Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public Advertiser update(Long id, Advertiser newAdvertiser, JwtAuthenticationToken token) throws EntityNotFoundException, AuthenticationException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!checkPrincipalAccess(advertiserById, token)) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        newAdvertiser.setId(id);
        advertiserRepository.save(newAdvertiser);
        return newAdvertiser;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public void delete(Long id, JwtAuthenticationToken token) throws EntityNotFoundException, AuthenticationException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!checkPrincipalAccess(advertiserById, token)) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        advertiserById.setAdvertiserDeleted(true);
        advertiserRepository.save(advertiserById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Advertiser getDeletedAdvertiser(Long id) throws EntityNotFoundException {
        return advertiserRepository.findByIdAndAdvertiserDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<Advertiser> getDeletedAdvertisers() {
        return advertiserRepository.findAllByAdvertiserDeletedTrue();
    }

    private boolean checkPrincipalAccess(Advertiser advertiser, JwtAuthenticationToken token) {
        String creatorName = advertiser.getCreator().getUsername();

        String principalName = token.getTokenAttributes().get("preferred_username").toString();
        boolean isAdmin = token.getTokenAttributes().get("realm_access").toString().contains(Role.ADMIN.name());

        if (isAdmin || creatorName.equals(principalName)) {
            return true;
        }

        return false;
    }
}
