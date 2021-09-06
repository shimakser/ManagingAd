package by.shimakser.service;

import by.shimakser.model.Advertiser;
import by.shimakser.repository.AdvertiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdvertiserService {

    private final AdvertiserRepository advertiserRepository;

    @Autowired
    public AdvertiserService(AdvertiserRepository advertiserRepository) {
        this.advertiserRepository = advertiserRepository;
    }

    public void add(Advertiser advertiser) {
        Optional<Advertiser> advertiserByTitle = Optional.ofNullable(advertiserRepository.findByAdvertiserTitle(advertiser.getAdvertiserTitle()));

        if (!advertiserByTitle.isPresent()) {
            advertiserRepository.save(advertiser);
        }
    }

    public Advertiser get(Long id) {
        Optional<Advertiser> advertiserById = advertiserRepository.findById(id);
        return advertiserById.get();
    }

    public Page<Advertiser> getAll(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> sortBy
    ) {
        return advertiserRepository.findAll(
                PageRequest.of(page.orElse(0),
                        size.orElse(advertiserRepository.findAll().size()),
                        Sort.Direction.ASC, sortBy.orElse("id"))
        );
    }

    public void update(Long id, Advertiser newAdvertiser) {
        Optional<Advertiser> advertiserById = advertiserRepository.findById(id);
        if (advertiserById.isPresent()) {
            newAdvertiser.setId(id);
            advertiserRepository.save(newAdvertiser);
        }
    }

    public void delete(Long id) {
        advertiserRepository.deleteById(id);
    }
}
