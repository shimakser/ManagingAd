package by.shimakser.controller.ad;

import by.shimakser.mapper.CampaignRecordMapper;
import by.shimakser.service.ad.CampaignFilterJooqService;
import by.shimakser.model.ad.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campaigns/jooq")
public class CampaignJooqController {

    private final CampaignRecordMapper campaignRecordMapper;

    private final CampaignFilterJooqService campaignFilterJooqService;

    @Autowired
    public CampaignJooqController(CampaignRecordMapper campaignRecordMapper, CampaignFilterJooqService campaignFilterJooqService) {
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
