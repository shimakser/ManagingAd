package by.shimakser.repository;

import by.shimakser.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    Campaign findByCampaignTitle(String title);
    Campaign findByIdAndCampaignDeletedTrue(Long id);
    List<Campaign> findAllByCampaignDeletedTrue();
}
