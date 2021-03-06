package by.shimakser.filter;

import by.shimakser.model.ad.Campaign;
import by.shimakser.model.ad.Campaign_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Set;

public class CampaignSpecifications {

    private CampaignSpecifications() {
    }

    public static Specification<Campaign> empty() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(
                        root.get(Campaign_.ID)
                );
    }

    public static Specification<Campaign> countryEqual(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Campaign_.COUNTRY),
                        "%" + country + "%"
                );
    }

    public static Specification<Campaign> ageMatch(Set<String> age) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        buildAgePredicates(age, root, criteriaBuilder)
                );
    }

    private static Predicate[] buildAgePredicates(Set<String> ageSet,
                                                  Root<Campaign> root,
                                                  CriteriaBuilder criteriaBuilder) {
        return ageSet
                .stream()
                .map(age ->
                        buildAgePredicate(age, root, criteriaBuilder)
                )
                .toArray(Predicate[]::new);
    }

    private static Predicate buildAgePredicate(String age,
                                               Root<Campaign> root,
                                               CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(
                root.get(Campaign_.AGE),
                age
        );
    }

    public static Specification<Campaign> deleteNotesEqual(String deletedNotes) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        root.get(Campaign_.CAMPAIGN_DELETE_NOTES),
                        "%" + deletedNotes + "%"
                );
    }

    public static Specification<Campaign> createdDateGreaterThanOrEqualTo(LocalDateTime createdDateFrom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get(Campaign_.CAMPAIGN_CREATED_DATE),
                        createdDateFrom
                );
    }

    public static Specification<Campaign> createdDateLessThanOrEqualTo(LocalDateTime createdDateTo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get(Campaign_.CAMPAIGN_CREATED_DATE),
                        createdDateTo
                );
    }
}
