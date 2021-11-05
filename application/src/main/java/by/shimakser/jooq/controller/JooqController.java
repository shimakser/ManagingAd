package by.shimakser.jooq.controller;

import by.shimakser.jooq.mapper.CampaignRecordMapper;
import by.shimakser.jooq.service.CampaignFilterJooqService;
import by.shimakser.mapper.CampaignMapper;
import by.shimakser.model.ad.Campaign;
import by.shimakser.model.tables.records.CampaignRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campaigns/jooq")
public class JooqController {

    private final CampaignRecordMapper campaignRecordMapper;

    private final CampaignFilterJooqService campaignFilterJooqService;

    @Autowired
    public JooqController(CampaignRecordMapper campaignRecordMapper, CampaignFilterJooqService campaignFilterJooqService) {
        this.campaignRecordMapper = campaignRecordMapper;
        this.campaignFilterJooqService = campaignFilterJooqService;
    }

    @GetMapping
    public List<Campaign> getAllCampaignByJooq() {
        return campaignRecordMapper.mapToListEntity(campaignFilterJooqService.getAllByJooq());
    }

    @GetMapping(value = "/{id}")
    public Campaign getCampaignByIdAndJooq(@PathVariable Integer id) {
        return campaignRecordMapper.mapToEntity(campaignFilterJooqService.getByJooq(id));
    }
}
