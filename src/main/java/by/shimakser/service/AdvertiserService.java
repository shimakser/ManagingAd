package by.shimakser.service;

import by.shimakser.model.Advertiser;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.AdvertiserRepository;
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
public class AdvertiserService {

    private final AdvertiserRepository advertiserRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdvertiserService(AdvertiserRepository advertiserRepository, UserRepository userRepository) {
        this.advertiserRepository = advertiserRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = AlreadyBoundException.class)
    public Advertiser add(Advertiser advertiser, Principal user) throws AlreadyBoundException {
        boolean isAdvertiserByTitleExist = advertiserRepository
                .existsAdvertiserByAdvertiserTitle(advertiser.getAdvertiserTitle());

        if (isAdvertiserByTitleExist) {
            throw new AlreadyBoundException("Entered title is already taken.");
        }
        User principalUser = userRepository.findByUsername(user.getName()).get();
        advertiser.setCreator(principalUser);
        advertiserRepository.save(advertiser);
        return advertiser;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public List<Advertiser> get(Long id) throws NotFoundException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertiser is not found."));
        List<Advertiser> advertiser = Stream.of(advertiserById)
                .filter(a -> a.isAdvertiserDeleted() == Boolean.FALSE)
                .collect(Collectors.toList());
        if (advertiser.isEmpty()) {
            throw new NotFoundException("User is not found.");
        }
        return advertiser;
    }

    @Transactional
    public List<Advertiser> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        return advertiserRepository.findAllByAdvertiserDeletedFalse(
                        PageRequest.of(page.orElse(0),
                                size.orElse(advertiserRepository.findAllByAdvertiserDeletedFalse().size()),
                                Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Transactional(rollbackFor = {NotFoundException.class, ForbiddenTargetException.class})
    public Advertiser update(Long id, Advertiser newAdvertiser, Principal creator) throws NotFoundException {
        checkAdvertiserByIdAndUserByPrincipal(id, creator);
        newAdvertiser.setId(id);
        advertiserRepository.save(newAdvertiser);
        return newAdvertiser;
    }

    @Transactional(rollbackFor = {NotFoundException.class, ForbiddenTargetException.class})
    public void delete(Long id, Principal creator) throws NotFoundException {
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

    public Advertiser checkAdvertiserByIdAndUserByPrincipal(Long id, Principal user) throws NotFoundException {
        Advertiser advertiserById = advertiserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertiser is not found."));

        User principalUser = userRepository.findByUsername(user.getName()).get();
        boolean checkAccess = principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(advertiserById.getCreator().getId());
        if (!checkAccess) {
            throw new ForbiddenTargetException("Insufficient rights to edit the advertiser.");
        }
        return advertiserById;
    }
}
