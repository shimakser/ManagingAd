package by.shimakser.repository;

import by.shimakser.model.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {
    Advertiser findByAdvertiserTitle(String title);
    Advertiser findByIdAndAdvertiserDeletedTrue(Long id);
    List<Advertiser> findAllByAdvertiserDeletedTrue();
}
