package by.shimakser.service;

import by.shimakser.filter.CampaignSpecifications;
import by.shimakser.filter.Filter;
import by.shimakser.filter.FilterRequest;
import by.shimakser.model.Campaign;
import by.shimakser.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampaignFilterService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignFilterService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Transactional
    public List<Campaign> getByFilter(FilterRequest filterRequest) {
        return campaignRepository.findAll(
                        buildSpecification(filterRequest.getFilter()),
                        buildPageable(filterRequest))
                .stream().collect(Collectors.toList());
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, pattern);
    }

    private Pageable buildPageable(FilterRequest filterRequest) {
        Integer pageNumber = filterRequest.getPage();
        Integer pageSize = Optional.ofNullable(filterRequest.getSize()).orElse(10);
        String sortFieldName = Optional.ofNullable(filterRequest.getSortBy()).orElse("id");

        if (pageNumber == null) {
            pageNumber = 0;
        } else {
            pageNumber -= 1;
        }

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
