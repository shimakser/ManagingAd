package by.shimakser.service;

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
            throw new AlreadyBoundException("Entered title is already taken.");
        }
        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException("Not authorized."));
        ;
        advertiser.setCreator(principalUser);
        advertiserRepository.save(advertiser);
        return advertiser;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public Advertiser get(Long id) throws NotFoundException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertiser is not found."));
        return Optional.of(advertiserById)
                .filter(not(Advertiser::isAdvertiserDeleted))
                .orElseThrow(() -> new NotFoundException("Advertiser is not found."));
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

    @Transactional(rollbackFor = {NotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public Advertiser update(Long id, Advertiser newAdvertiser, Principal creator)
            throws NotFoundException, AuthenticationException {
        checkAdvertiserByIdAndUserByPrincipal(id, creator);
        newAdvertiser.setId(id);
        advertiserRepository.save(newAdvertiser);
        return newAdvertiser;
    }

    @Transactional(rollbackFor = {NotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public void delete(Long id, Principal creator) throws NotFoundException, AuthenticationException {
        Advertiser advertiserById = checkAdvertiserByIdAndUserByPrincipal(id, creator);
        advertiserById.setAdvertiserDeleted(true);
        advertiserRepository.save(advertiserById);
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public Advertiser getDeletedAdvertiser(Long id) throws NotFoundException {
        return advertiserRepository.findByIdAndAdvertiserDeletedTrue(id)
                .orElseThrow(() -> new NotFoundException("Deleted advertiser is not found."));
    }

    @Transactional
    public List<Advertiser> getDeletedAdvertisers() {
        return advertiserRepository.findAllByAdvertiserDeletedTrue();
    }

    public Advertiser checkAdvertiserByIdAndUserByPrincipal(Long id, Principal user)
            throws NotFoundException, AuthenticationException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertiser is not found."));

        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException("Not authorized."));
        boolean checkAccess = principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(advertiserById.getCreator().getId());
        if (!checkAccess) {
            throw new AuthenticationException("Insufficient rights to edit the advertiser.");
        }
        return advertiserById;
    }
}
