package by.shimakser.repository.ad;

import by.shimakser.model.ad.Advertiser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {
    boolean existsAdvertiserByAdvertiserTitle(String title);

    Optional<Advertiser> findByIdAndAdvertiserDeletedTrue(Long id);

    List<Advertiser> findAllByAdvertiserDeletedTrue();

    List<Advertiser> findAllByAdvertiserDeletedFalse(Pageable pageable);
}
