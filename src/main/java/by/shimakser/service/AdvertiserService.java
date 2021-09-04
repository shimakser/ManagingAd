package by.shimakser.service;

import by.shimakser.model.Advertiser;
import by.shimakser.repository.AdvertiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvertiserService {

    private final AdvertiserRepository advertiserRepository;

    @Autowired
    public AdvertiserService(AdvertiserRepository advertiserRepository) {
        this.advertiserRepository = advertiserRepository;
    }

    public void add(Advertiser advertiser) {
        Advertiser advertiserByTitle = advertiserRepository.findByAdvertiserTitle(advertiser.getAdvertiserTitle());

        if (advertiserByTitle == null) {
            advertiserRepository.save(advertiser);
        }
    }

    public Advertiser get(Long id) {
        Optional<Advertiser> advertiserById = advertiserRepository.findById(id);
        return advertiserById.get();
    }

    public List<Advertiser> getAll() {
        List<Advertiser> allAdvertisers = (List<Advertiser>) advertiserRepository.findAll();
        return allAdvertisers;
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
