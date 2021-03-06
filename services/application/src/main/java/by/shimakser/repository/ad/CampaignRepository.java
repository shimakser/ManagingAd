package by.shimakser.repository.ad;

import by.shimakser.model.ad.Campaign;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long>, JpaSpecificationExecutor<Campaign> {
    boolean existsCampaignByCampaignTitle(String title);

    Optional<Campaign> findByIdAndCampaignDeletedTrue(Long id);

    List<Campaign> findAllByCampaignDeletedTrue();

    List<Campaign> findAllByCampaignDeletedFalse(Specification<Campaign> spec, Pageable pageable);
}
