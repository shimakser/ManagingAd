package by.shimakser.service.ad;

import by.shimakser.keycloak.service.SecurityService;
import by.shimakser.model.ad.Advertiser;
import by.shimakser.model.ad.Campaign;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.CampaignRepository;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private SecurityService securityService;

    @InjectMocks
    private CampaignService campaignService;

    private static User USER;
    private static Advertiser ADVERTISER;
    private static Campaign CAMPAIGN;
    private static Long CAMPAIGN_ID;

    @BeforeEach
    void init() {
        USER = new User(1L, "user", "mail", "pw", Role.USER, Boolean.FALSE);

        ADVERTISER = new Advertiser();
        ADVERTISER.setCreator(USER);

        CAMPAIGN = new Campaign();
        CAMPAIGN.setId(1L);
        CAMPAIGN.setCampaignTitle("title");
        CAMPAIGN_ID = CAMPAIGN.getId();
        CAMPAIGN.setAdvertiser(ADVERTISER);
    }

    /**
     * {@link CampaignService#add(Campaign)}
     */
    @Test
    void Given_CheckIsTitleBound_When_CreateCampaign_Then_CheckIsCorrectlyCreatedCampaign() {
        // given
        given(campaignRepository.existsCampaignByCampaignTitle(CAMPAIGN.getCampaignTitle())).willReturn(false);

        // when
        Campaign campaign = null;
        try {
            campaign = campaignService.add(CAMPAIGN);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        }

        // then
        then(campaignRepository)
                .should()
                .existsCampaignByCampaignTitle(CAMPAIGN.getCampaignTitle());
        assertEquals(campaign, CAMPAIGN);
    }

    /**
     * {@link CampaignService#add(Campaign)}
     */
    @Test
    void Given_CheckIsTitleBound_When_CreateCampaign_Then_CatchExceptionByAlreadyBoundTitle() {
        // given
        given(campaignRepository.existsCampaignByCampaignTitle(CAMPAIGN.getCampaignTitle())).willReturn(true);

        // when
        BDDCatchException.when(() -> campaignService.add(CAMPAIGN));

        // then
        BDDAssertions.then(caughtException())
                .isInstanceOf(EntityExistsException.class);
        then(campaignRepository)
                .should(never())
                .save(CAMPAIGN);
    }

    /**
     * {@link CampaignService#get(Long)}
     */
    @Test
    void Given_SearchCampaignById_When_GetCampaign_Then_CheckIsCorrectlySearchedCampaign() {
        // given
        given(campaignRepository.findById(CAMPAIGN_ID))
                .willReturn(Optional.of(CAMPAIGN));

        // when
        Campaign campaign = campaignService.get(CAMPAIGN_ID);

        // then
        then(campaignRepository)
                .should()
                .findById(CAMPAIGN_ID);
        assertEquals(CAMPAIGN, campaign);
    }

    /**
     * {@link CampaignService#get(Long)}
     */
    @Test
    void Given_SearchCampaignById_When_GetCampaign_Then_CatchExceptionByNotExistCampaign() {
        // given
        given(campaignRepository.findById(CAMPAIGN_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> campaignService.get(CAMPAIGN_ID));

        // then
        BDDAssertions.then(caughtException())
                .isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * {@link CampaignService#update(Long, Campaign)}
     */
    @Test
    void Given_SearchCampaignById_When_UpdateCampaign_Then_CheckIsCorrectlyUpdatedCampaign() {
        // given
        Campaign testCampaign = new Campaign();
        testCampaign.setCampaignTitle("test");
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.of(CAMPAIGN));
        given(securityService.checkPrincipalAccess(USER.getUsername())).willReturn(true);

        // when
        Campaign updatedCampaign = null;
        try {
            updatedCampaign = campaignService.update(CAMPAIGN_ID, testCampaign);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(campaignRepository)
                .should()
                .findById(CAMPAIGN_ID);
        then(campaignRepository)
                .should()
                .save(testCampaign);
        assertEquals(updatedCampaign.getCampaignTitle(), testCampaign.getCampaignTitle());
    }

    /**
     * {@link CampaignService#update(Long, Campaign)}
     */
    @Test
    void Given_SearchCampaignById_When_UpdateCampaign_Then_CatchExceptionByNotExistCampaign() {
        // given
        Campaign testCampaign = new Campaign();
        given(campaignRepository.findById(CAMPAIGN_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> campaignService.update(CAMPAIGN_ID, testCampaign));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
        then(campaignRepository)
                .should(never())
                .save(testCampaign);
    }

    /**
     * {@link CampaignService#delete(Long)}
     */
    @Test
    void Given_SearchCampaignById_When_DeleteCampaign_Then_CheckIsCorrectlyDeletedCampaign() {
        // given
        given(securityService.checkPrincipalAccess(USER.getUsername())).willReturn(true);
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.of(CAMPAIGN));

        // when
        try {
            campaignService.delete(CAMPAIGN_ID);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(campaignRepository)
                .should()
                .save(CAMPAIGN);
        assertEquals(Boolean.TRUE, CAMPAIGN.isCampaignDeleted());
    }

    /**
     * {@link CampaignService#delete(Long)}
     */
    @Test
    void Given_SearchCampaignById_When_DeleteCampaign_Then_CatchExceptionByNotExistCampaign() {
        // given
        given(campaignRepository.findById(CAMPAIGN_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> campaignService.delete(CAMPAIGN_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
        then(campaignRepository)
                .should(never())
                .save(CAMPAIGN);
        assertEquals(Boolean.FALSE, CAMPAIGN.isCampaignDeleted());
    }

    /**
     * {@link CampaignService#getDeletedCampaign(Long)}
     */
    @Test
    void Given_SearchDeletedCampaignById_When_GetDeletedCampaign_Then_CheckIsCorrectlySearchedCampaign() {
        // given
        given(campaignRepository.findByIdAndCampaignDeletedTrue(CAMPAIGN_ID))
                .willReturn(Optional.of(CAMPAIGN));

        // when
        Campaign deletedCampaign = campaignService.getDeletedCampaign(CAMPAIGN_ID);

        // then
        then(campaignRepository)
                .should()
                .findByIdAndCampaignDeletedTrue(CAMPAIGN_ID);
        assertEquals(CAMPAIGN, deletedCampaign);
    }

    /**
     * {@link CampaignService#getDeletedCampaign(Long)}
     */
    @Test
    void Given_SearchDeletedCampaignById_When_GetDeletedCampaign_Then_CatchExceptionByNotExistCampaign() {
        // given
        given(campaignRepository.findByIdAndCampaignDeletedTrue(CAMPAIGN_ID)).willReturn(Optional.empty());

        // when
        BDDCatchException.when(() -> campaignService.getDeletedCampaign(CAMPAIGN_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * {@link CampaignService#getDeletedCampaigns()}
     */
    @Test
    void Given_SearchAllDeletedCampaigns_When_GetDeletedCampaigns_Then_CheckIsCorrectlySearchedCampaigns() {
        // given
        given(campaignRepository.findAllByCampaignDeletedTrue())
                .willReturn(List.of(CAMPAIGN));

        // when
        List<Campaign> deletedCampaigns = campaignService.getDeletedCampaigns();

        // then
        then(campaignRepository)
                .should()
                .findAllByCampaignDeletedTrue();
        assertEquals(List.of(CAMPAIGN), deletedCampaigns);
    }
}