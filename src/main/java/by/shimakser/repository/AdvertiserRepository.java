package by.shimakser.repository;

import by.shimakser.model.Advertiser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertiserRepository extends CrudRepository<Advertiser, Long> {
    Advertiser findByAdvertiserTitle(String title);
}
