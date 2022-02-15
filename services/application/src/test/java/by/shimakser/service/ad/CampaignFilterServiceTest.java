package by.shimakser.service.ad;

import by.shimakser.dto.CampaignFilterRequest;
import by.shimakser.filter.campaign.CampaignSpecifications;
import by.shimakser.model.ad.Campaign;
import by.shimakser.repository.ad.CampaignRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
class CampaignFilterServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignFilterService campaignFilterService;

    private static final Campaign CAMPAIGN = new Campaign();
    private static final CampaignFilterRequest FILTER_REQUEST = new CampaignFilterRequest(0, 10, Sort.by("id"));

    @Test
    void getByFilter() {
        // given
        given(campaignRepository.findAll(
                CampaignSpecifications.empty(),
                PageRequest.of(0, 10, Sort.by("id"))
        )).willReturn(new PageImpl<>(List.of(CAMPAIGN)));

        // when
        List<Campaign> campaigns = campaignFilterService.getByFilter(FILTER_REQUEST);

        // then
        then(campaignRepository)
                .should()
                .findAll(
                        CampaignSpecifications.empty(),
                        PageRequest.of(0, 10, Sort.by("id"))
                );
        assertEquals(campaigns, List.of(CAMPAIGN));
    }
}