package by.shimakser.repository;

import by.shimakser.model.Campaign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends CrudRepository<Campaign, Long> {
    Campaign findByCampaignTitle(String title);
}
