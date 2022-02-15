package by.shimakser.service.ad;

import by.shimakser.model.ad.Advertiser;
import by.shimakser.model.ad.Campaign;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private JwtAuthenticationToken token;

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

    @Test
    void add() {
        // given
        given(campaignRepository.existsCampaignByCampaignTitle(CAMPAIGN.getCampaignTitle())).willReturn(false);

        // when
        Campaign campaign = null;
        try {
            campaign = campaignService.add(CAMPAIGN);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        // then
        then(campaignRepository)
                .should()
                .existsCampaignByCampaignTitle(CAMPAIGN.getCampaignTitle());
        assertEquals(campaign, CAMPAIGN);
    }

    @Test
    void add_WithExistTitle() {
        // given
        given(campaignRepository.existsCampaignByCampaignTitle(CAMPAIGN.getCampaignTitle())).willReturn(true);

        // then
        assertThrows(AlreadyBoundException.class, () -> campaignService.add(CAMPAIGN));
        then(campaignRepository)
                .should(never())
                .save(CAMPAIGN);
    }

    @Test
    void get() {
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

    @Test
    void get_WithIncorrectId() {
        // given
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> campaignService.get(CAMPAIGN_ID));
        then(campaignRepository)
                .should(never())
                .save(CAMPAIGN);
    }

    @Test
    void update() {
        // given
        Campaign testCampaign = new Campaign();
        testCampaign.setCampaignTitle("test");
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", USER.getUsername());
        tokenAttributes.put("realm_access", "USER");
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.of(CAMPAIGN));
        given(token.getTokenAttributes()).willReturn(tokenAttributes);

        // when
        Campaign updatedCampaign = null;
        try {
            updatedCampaign = campaignService.update(CAMPAIGN_ID, testCampaign, token);
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

    @Test
    void update_WithIncorrectId() {
        // given
        Campaign testCampaign = new Campaign();
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> campaignService.update(CAMPAIGN_ID, testCampaign, token));
        then(campaignRepository)
                .should(never())
                .save(testCampaign);
    }

    @Test
    void delete() {
        // given
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", USER.getUsername());
        tokenAttributes.put("realm_access", "USER");
        given(token.getTokenAttributes()).willReturn(tokenAttributes);
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.of(CAMPAIGN));

        // when
        try {
            campaignService.delete(CAMPAIGN_ID, token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(campaignRepository)
                .should()
                .save(CAMPAIGN);
        assertEquals(Boolean.TRUE, CAMPAIGN.isCampaignDeleted());
    }

    @Test
    void delete_WithIncorrectId() {
        // given
        given(campaignRepository.findById(CAMPAIGN_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> campaignService.delete(CAMPAIGN_ID, token));
        then(campaignRepository)
                .should(never())
                .save(CAMPAIGN);
        assertEquals(Boolean.FALSE, CAMPAIGN.isCampaignDeleted());
    }

    @Test
    void getDeletedCampaign() {
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

    @Test
    void getDeletedCampaign_WithIncorrectId() {
        // given
        given(campaignRepository.findByIdAndCampaignDeletedTrue(CAMPAIGN_ID))
                .willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> campaignService.getDeletedCampaign(CAMPAIGN_ID));
        then(campaignRepository)
                .should()
                .findByIdAndCampaignDeletedTrue(CAMPAIGN_ID);
    }

    @Test
    void getDeletedCampaigns() {
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