package by.shimakser.service.ad;

import by.shimakser.dto.CampaignFilterRequest;
import by.shimakser.filter.campaign.CampaignSpecifications;
import by.shimakser.model.ad.Campaign;
import by.shimakser.repository.ad.CampaignRepository;
import by.shimakser.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignFilterService extends FilterService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignFilterService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Transactional
    public List<Campaign> getByFilter(CampaignFilterRequest campaignFilterRequest) {
        return campaignRepository.findAll(
                        buildSpecification(campaignFilterRequest),
                        buildPageable(campaignFilterRequest))
                .stream().collect(Collectors.toList());
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, pattern);
    }

    private Specification<Campaign> buildSpecification(CampaignFilterRequest campaignFilterRequest) {
        Specification<Campaign> specification = CampaignSpecifications.empty();

        if (campaignFilterRequest == null) {
            return specification;
        }

        if (campaignFilterRequest.getCountry() != null) {
            specification = specification.and(
                    CampaignSpecifications.countryEqual(campaignFilterRequest.getCountry())
            );
        }

        if (campaignFilterRequest.getAge() != null) {
            specification = specification.and(
                    CampaignSpecifications.ageMatch(campaignFilterRequest.getAge())
            );
        }
        if (campaignFilterRequest.getCampaignDeleteNotes() != null) {
            specification = specification.and(
                    CampaignSpecifications.deleteNotesEqual(campaignFilterRequest.getCampaignDeleteNotes())
            );
        }

        if (campaignFilterRequest.getCreatedDateFrom() != null) {
            LocalDateTime dateCreatedFrom = convertToLocalDateTime(campaignFilterRequest.getCreatedDateFrom());
            specification = specification.and(
                    CampaignSpecifications.createdDateGreaterThanOrEqualTo(dateCreatedFrom)
            );
        }

        if (campaignFilterRequest.getCreatedDateTo() != null) {
            LocalDateTime dateCreatedTo = convertToLocalDateTime(campaignFilterRequest.getCreatedDateTo());
            specification = specification.and(
                    CampaignSpecifications.createdDateLessThanOrEqualTo(dateCreatedTo)
            );
        }

        return specification;
    }
}
