package by.shimakser.repository;

import by.shimakser.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    boolean existsCampaignByCampaignTitle(String title);
    Optional<Campaign> findByIdAndCampaignDeletedTrue(Long id);
    List<Campaign> findAllByCampaignDeletedTrue();
}
