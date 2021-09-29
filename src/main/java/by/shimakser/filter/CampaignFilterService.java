package by.shimakser.filter;

import by.shimakser.model.Campaign;
import by.shimakser.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignFilterService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignFilterService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> getByFilter(FilterRequest filterRequest) {
        return campaignRepository.findAll(buildSpecification(filterRequest.getFilter()), buildPageable(filterRequest))
                .stream().collect(Collectors.toList());
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(date, pattern);
        return parsedDate;
    }

    private Pageable buildPageable(FilterRequest filterRequest) {
        int pageNumber = filterRequest.getNumOfPage() - 1;
        int pageSize = filterRequest.getPageSize();
        String sortFieldName = filterRequest.getSortBy();

        return PageRequest.of(pageNumber, pageSize, Sort.by(sortFieldName));
    }

    private Specification<Campaign> buildSpecification(Filter filter) {
        Specification<Campaign> specification = CampaignSpecifications.empty();

        if (filter == null) {
            return specification;
        }

        if (filter.getCountry() != null) {
            specification = specification.and(
                    CampaignSpecifications.countryEqual(filter.getCountry())
            );
        }

        if (filter.getAge() != null) {
            specification = specification.and(
                    CampaignSpecifications.ageMatch(filter.getAge())
            );
        }
        if (filter.getCampaignDeleteNotes() != null) {
            specification = specification.and(
                    CampaignSpecifications.deleteNotesEqual(filter.getCampaignDeleteNotes())
            );
        }

        if (filter.getCreatedDateFrom() != null) {
            LocalDateTime dateCreatedFrom = convertToLocalDateTime(filter.getCreatedDateFrom());
            specification = specification.and(
                    CampaignSpecifications.createdDateGreaterThanOrEqualTo(dateCreatedFrom)
            );
        }

        if (filter.getCreatedDateTo() != null) {
            LocalDateTime dateCreatedTo = convertToLocalDateTime(filter.getCreatedDateTo());
            specification = specification.and(
                    CampaignSpecifications.createdDateLessThanOrEqualTo(dateCreatedTo)
            );
        }

        return specification;
    }


}
