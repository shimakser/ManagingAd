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

    private static final int PAGE = 0;
    private static final int SIZE = 1;
    private static final String SORT = "id";

    private static final Campaign CAMPAIGN = new Campaign();
    private static final CampaignFilterRequest FILTER_REQUEST = new CampaignFilterRequest(PAGE, SIZE, Sort.by(SORT));
    private static final PageRequest PAGE_REQUEST = PageRequest.of(PAGE, SIZE, Sort.by(SORT));

    /**
     * {@link CampaignFilterService#getByFilter(CampaignFilterRequest)}
     */
    @Test
    void Given_SearchCampaignsByEmptyFilter_When_GetCampaigns_Then_CheckIsCorrectlySearchedCampaigns() {
        // given
        given(campaignRepository.findAll(CampaignSpecifications.empty(), PAGE_REQUEST))
                .willReturn(new PageImpl<>(List.of(CAMPAIGN)));

        // when
        List<Campaign> campaigns = campaignFilterService.getByFilter(FILTER_REQUEST);

        // then
        then(campaignRepository)
                .should()
                .findAll(CampaignSpecifications.empty(), PAGE_REQUEST);
        assertEquals(campaigns, List.of(CAMPAIGN));
    }
}