package by.shimakser.service.ad;

import by.shimakser.model.ad.Advertiser;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.AdvertiserRepository;
import by.shimakser.repository.ad.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
class AdvertiserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertiserRepository advertiserRepository;
    @Mock
    private JwtAuthenticationToken token;

    @InjectMocks
    private AdvertiserService advertiserService;

    private static Advertiser ADVERTISER;
    private static Long ADVERTISER_ID;
    private static User USER;

    @BeforeEach
    void init() {
        USER = new User(1L, "user", "mail", "pw", Role.USER, Boolean.FALSE);
        ADVERTISER = new Advertiser(1L, "title", "description", USER, false);
        ADVERTISER_ID = ADVERTISER.getId();
    }

    @Test
    void add() {
        // given
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", USER.getUsername());
        tokenAttributes.put("realm_access", "USER");
        given(advertiserRepository.existsAdvertiserByAdvertiserTitle(ADVERTISER.getAdvertiserTitle())).willReturn(false);
        given(token.getTokenAttributes()).willReturn(tokenAttributes);
        given(userRepository.findByUsername(USER.getUsername())).willReturn(Optional.of(USER));

        // when
        Advertiser advertiser = null;
        try {
            advertiser = advertiserService.add(ADVERTISER, token);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        // then
        then(advertiserRepository)
                .should()
                .existsAdvertiserByAdvertiserTitle(ADVERTISER.getAdvertiserTitle());
        then(userRepository)
                .should()
                .findByUsername(USER.getUsername());
        then(advertiserRepository)
                .should()
                .save(ADVERTISER);
        assertEquals(advertiser.getAdvertiserDescription(), ADVERTISER.getAdvertiserDescription());
    }

    @Test
    void add_WithExistTitle() {
        // given
        given(advertiserRepository.existsAdvertiserByAdvertiserTitle(ADVERTISER.getAdvertiserTitle()))
                .willReturn(true);

        // then
        assertThrows(AlreadyBoundException.class, () -> advertiserService.add(ADVERTISER, token));
        then(advertiserRepository)
                .should(never())
                .save(ADVERTISER);
    }

    @Test
    void get() {
        // given
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.of(ADVERTISER));

        // when
        Advertiser advertiser = advertiserService.get(ADVERTISER_ID);

        // then
        then(advertiserRepository)
                .should()
                .findById(ADVERTISER_ID);
        assertEquals(ADVERTISER, advertiser);
    }

    @Test
    void get_WithIncorrectId() {
        // given
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> advertiserService.get(ADVERTISER_ID));
        then(advertiserRepository)
                .should()
                .findById(ADVERTISER_ID);
    }

    @Test
    void getAll() {
        // given
        given(advertiserRepository.findAllByAdvertiserDeletedFalse(PageRequest.of(0, 1, Sort.Direction.ASC, "id")))
                .willReturn(List.of(ADVERTISER));

        // when
        List<Advertiser> advertisers = advertiserService.getAll(Optional.of(0), Optional.of(1), Optional.of("id"));

        // then
        then(advertiserRepository)
                .should()
                .findAllByAdvertiserDeletedFalse(PageRequest.of(0, 1, Sort.Direction.ASC, "id"));
        assertEquals(advertisers, List.of(ADVERTISER));
    }

    @Test
    void update() {
        // given
        Advertiser testAdvertiser = new Advertiser();
        testAdvertiser.setAdvertiserTitle("test");
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", USER.getUsername());
        tokenAttributes.put("realm_access", "USER");
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.of(ADVERTISER));
        given(token.getTokenAttributes()).willReturn(tokenAttributes);

        // when
        Advertiser updatedAdvertiser = null;
        try {
            updatedAdvertiser = advertiserService.update(ADVERTISER_ID, testAdvertiser, token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(advertiserRepository)
                .should()
                .findById(ADVERTISER_ID);
        then(advertiserRepository)
                .should()
                .save(testAdvertiser);
        assertEquals(updatedAdvertiser.getAdvertiserTitle(), testAdvertiser.getAdvertiserTitle());
    }

    @Test
    void update_WithIncorrectId() {
        // given
        Advertiser testAdvertiser = new Advertiser();
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> advertiserService.update(ADVERTISER_ID, testAdvertiser, token));
        then(advertiserRepository)
                .should(never())
                .save(testAdvertiser);
    }

    @Test
    void delete() {
        // given
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", USER.getUsername());
        tokenAttributes.put("realm_access", "USER");
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.of(ADVERTISER));
        given(token.getTokenAttributes()).willReturn(tokenAttributes);

        // when
        try {
            advertiserService.delete(ADVERTISER_ID, token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(advertiserRepository)
                .should()
                .save(ADVERTISER);
        assertEquals(Boolean.TRUE, ADVERTISER.isAdvertiserDeleted());
    }

    @Test
    void delete_WithIncorrectId() {
        // given
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> advertiserService.delete(ADVERTISER_ID, token));
        then(advertiserRepository)
                .should(never())
                .save(ADVERTISER);
        assertEquals(Boolean.FALSE, ADVERTISER.isAdvertiserDeleted());
    }


    @Test
    void getDeletedAdvertiser() {
        // given
        given(advertiserRepository.findByIdAndAdvertiserDeletedTrue(ADVERTISER_ID))
                .willReturn(Optional.of(ADVERTISER));

        // when
        Advertiser deletedAdvertiser = advertiserService.getDeletedAdvertiser(ADVERTISER_ID);

        // then
        then(advertiserRepository)
                .should()
                .findByIdAndAdvertiserDeletedTrue(ADVERTISER_ID);
        assertEquals(ADVERTISER, deletedAdvertiser);
    }

    @Test
    void getDeletedCampaign_WithIncorrectId() {
        // given
        given(advertiserRepository.findByIdAndAdvertiserDeletedTrue(ADVERTISER_ID))
                .willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> advertiserService.getDeletedAdvertiser(ADVERTISER_ID));
        then(advertiserRepository)
                .should()
                .findByIdAndAdvertiserDeletedTrue(ADVERTISER_ID);
    }

    @Test
    void getDeletedAdvertisers() {
        // given
        given(advertiserRepository.findAllByAdvertiserDeletedTrue())
                .willReturn(List.of(ADVERTISER));

        // when
        List<Advertiser> deletedAdvertisers = advertiserService.getDeletedAdvertisers();

        // then
        then(advertiserRepository)
                .should()
                .findAllByAdvertiserDeletedTrue();
        assertEquals(List.of(ADVERTISER), deletedAdvertisers);
    }
}