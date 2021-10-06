package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.Advertiser;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.AdvertiserRepository;
import by.shimakser.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.security.Principal;
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
    public Advertiser add(Advertiser advertiser, Principal user) throws AlreadyBoundException {
        boolean isAdvertiserByTitleExist = advertiserRepository
                .existsAdvertiserByAdvertiserTitle(advertiser.getAdvertiserTitle());

        if (isAdvertiserByTitleExist) {
            throw new AlreadyBoundException(ExceptionText.AlreadyBound.getExceptionText());
        }
        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException(ExceptionText.AuthorizationService.getExceptionText()));
        ;
        advertiser.setCreator(principalUser);
        advertiserRepository.save(advertiser);
        return advertiser;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Advertiser get(Long id) throws EntityNotFoundException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
        return Optional.of(advertiserById)
                .filter(not(Advertiser::isAdvertiserDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional
    public List<Advertiser> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        return advertiserRepository.findAllByAdvertiserDeletedFalse(
                PageRequest.of(page.orElse(0),
                        size.orElse(10),
                        Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public Advertiser update(Long id, Advertiser newAdvertiser, Principal creator)
            throws EntityNotFoundException, AuthenticationException {
        checkAdvertiserByIdAndUserByPrincipal(id, creator);
        newAdvertiser.setId(id);
        advertiserRepository.save(newAdvertiser);
        return newAdvertiser;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public void delete(Long id, Principal creator) throws EntityNotFoundException, AuthenticationException {
        Advertiser advertiserById = checkAdvertiserByIdAndUserByPrincipal(id, creator);
        advertiserById.setAdvertiserDeleted(true);
        advertiserRepository.save(advertiserById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public Advertiser getDeletedAdvertiser(Long id) throws EntityNotFoundException {
        return advertiserRepository.findByIdAndAdvertiserDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional
    public List<Advertiser> getDeletedAdvertisers() {
        return advertiserRepository.findAllByAdvertiserDeletedTrue();
    }

    public Advertiser checkAdvertiserByIdAndUserByPrincipal(Long id, Principal user)
            throws EntityNotFoundException, AuthenticationException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));

        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException(ExceptionText.AuthorizationService.getExceptionText()));
        boolean checkAccess = principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(advertiserById.getCreator().getId());
        if (!checkAccess) {
            throw new AuthenticationException(ExceptionText.Authentication.getExceptionText());
        }
        return advertiserById;
    }
}
