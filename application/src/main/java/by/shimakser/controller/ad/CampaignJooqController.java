package by.shimakser.controller.ad;

import by.shimakser.filter.campaign.CampaignFilterRequest;
import by.shimakser.mapper.CampaignRecordMapper;
import by.shimakser.model.ad.Campaign;
import by.shimakser.service.ad.CampaignFilterJooqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campaigns/filter")
public class CampaignJooqController {

    private final CampaignRecordMapper campaignRecordMapper;

    private final CampaignFilterJooqService campaignFilterJooqService;

    @Autowired
    public CampaignJooqController(CampaignRecordMapper campaignRecordMapper, CampaignFilterJooqService campaignFilterJooqService) {
        this.campaignRecordMapper = campaignRecordMapper;
        this.campaignFilterJooqService = campaignFilterJooqService;
    }

    @PostMapping(value = "/jooq")
    public List<Campaign> getCampaignByFilter(@RequestBody CampaignFilterRequest campaignFilterRequest) {
        return campaignRecordMapper.mapToListEntity(campaignFilterJooqService
                .getAllByFilterWithJooq(campaignFilterRequest));
    }
}
