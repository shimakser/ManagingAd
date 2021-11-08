package by.shimakser.service.jooq;

import by.shimakser.filter.campaign.CampaignFilterRequest;
import by.shimakser.model.tables.records.CampaignRecord;
import by.shimakser.service.FilterService;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.shimakser.model.Tables.CAMPAIGN;

@Service
public class CampaignFilterJooqService extends FilterService {

    private final DSLContext context;

    @Autowired
    public CampaignFilterJooqService(DSLContext context) {
        this.context = context;
    }

    @Transactional
    public List<CampaignRecord> getAllByFilterWithJooq(CampaignFilterRequest campaignFilterRequest) {
        return context
                .select(CAMPAIGN.fields())
                .from(CAMPAIGN)
                .where(buildCondition(campaignFilterRequest))
                .orderBy(CAMPAIGN.ID.asc())
                .limit(campaignFilterRequest.getSize())
                .offset(campaignFilterRequest.getPage())
                .fetchInto(CampaignRecord.class);
    }

    private Condition buildCondition(CampaignFilterRequest campaignFilterRequest) {
        Condition condition = null;

        if (campaignFilterRequest.getCountry() != null) {
            condition = append(condition, CAMPAIGN.COUNTRY.eq(campaignFilterRequest.getCountry()));
        }

        if (campaignFilterRequest.getCampaignDeleteNotes() != null) {
            condition = append(condition, CAMPAIGN.CAMPAIGN_DELETE_NOTES.eq(campaignFilterRequest.getCampaignDeleteNotes()));
        }

        if (campaignFilterRequest.getCreatedDateFrom() != null) {
            condition = append(condition, CAMPAIGN.CAMPAIGN_CREATED_DATE.
                    eq(convertToLocalDateTime(campaignFilterRequest.getCreatedDateFrom())));
        }

        if (campaignFilterRequest.getCreatedDateTo() != null) {
            condition = append(condition,
                    CAMPAIGN.CAMPAIGN_CREATED_DATE
                            .eq(convertToLocalDateTime(campaignFilterRequest.getCreatedDateTo())));
        }

        if (campaignFilterRequest.getAge() != null) {
            for (String age : campaignFilterRequest.getAge()) {
                condition = append(condition, CAMPAIGN.AGE.eq(age));
            }
        }

        return condition;
    }

    private Condition append(Condition base, Condition next) {
        if (base == null) {
            return next;
        } else {
            return base.and(next);
        }
    }
}
