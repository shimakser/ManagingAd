package by.shimakser.service;

import by.shimakser.model.Advertiser;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.AdvertiserRepository;
import by.shimakser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<HttpStatus> add(Advertiser advertiser) {
        Optional<Advertiser> advertiserByTitle = advertiserRepository
                .findByAdvertiserTitle(advertiser.getAdvertiserTitle());

        if (advertiserByTitle.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        advertiserRepository.save(advertiser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<List<Advertiser>> get(Long id) {
        Optional<Advertiser> advertiserById = advertiserRepository.findById(id);
        List<Advertiser> advertiser = Stream.of(advertiserById.get()).filter(a -> a.isAdvertiserDeleted() == Boolean.FALSE).collect(Collectors.toList());
        if (advertiser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(advertiser, HttpStatus.OK);
    }

    public ResponseEntity<List<Advertiser>> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        Page<Advertiser> advertisers = advertiserRepository.findAll(
                PageRequest.of(page.orElse(0),
                        size.orElse(advertiserRepository.findAll().size()),
                        Sort.Direction.ASC, sortBy.orElse("id"))
        );
        if (advertisers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>((List<Advertiser>) advertisers, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> update(Long id, Advertiser newAdvertiser, Principal creator) {
        if (!findAdvertiserByIdAndUserByPrincipal(id, creator)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newAdvertiser.setId(id);
        advertiserRepository.save(newAdvertiser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> delete(Long id, Principal creator) {
        if (!findAdvertiserByIdAndUserByPrincipal(id, creator)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        advertiserRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Advertiser> getDeletedAdvertiser(Long id) {
        Optional<Advertiser> deletedAdvertiserById = advertiserRepository.findByIdAndAdvertiserDeletedTrue(id);
        if (!deletedAdvertiserById.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deletedAdvertiserById.get(), HttpStatus.OK);
    }

    public ResponseEntity<List<Advertiser>> getDeletedAdvertisers() {
        List<Advertiser> deletedAllAdvertisersById = advertiserRepository.findAllByAdvertiserDeletedTrue();
        if (deletedAllAdvertisersById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deletedAllAdvertisersById, HttpStatus.OK);
    }

    public boolean findAdvertiserByIdAndUserByPrincipal(Long id, Principal user) {
        Optional<Advertiser> advertiserById = advertiserRepository.findById(id);
        if (!advertiserById.isPresent()) {
            return false;
        }
        User principalUser = userRepository.findByUsername(user.getName()).get();
        return (principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(advertiserById.get().getCreator().getId()));
    }
}
