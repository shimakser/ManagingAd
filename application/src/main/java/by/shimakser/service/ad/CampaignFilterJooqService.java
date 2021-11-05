package by.shimakser.service.ad;

import by.shimakser.model.Tables;
import by.shimakser.model.tables.records.CampaignRecord;
import by.shimakser.service.FilterService;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.shimakser.model.Tables.CAMPAIGN;

@Service
public class CampaignFilterJooqService extends FilterService {

    private final DSLContext context;

    @Autowired
    public CampaignFilterJooqService(DSLContext context) {
        this.context = context;
    }

    public List<CampaignRecord> getAllByJooq() {
        return (List<CampaignRecord>) context
                .fetch(Tables.CAMPAIGN);
    }

    public CampaignRecord getByJooq(Integer id) {
        return context
                .fetchOne(Tables.CAMPAIGN, CAMPAIGN.ID.eq(id));
    }
}
