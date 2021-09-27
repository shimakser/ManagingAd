package by.shimakser.repository;

import by.shimakser.model.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {
    boolean existsAdvertiserByAdvertiserTitle(String title);
    Optional<Advertiser> findByIdAndAdvertiserDeletedTrue(Long id);
    List<Advertiser> findAllByAdvertiserDeletedTrue();
}
