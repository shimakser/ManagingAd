package by.shimakser.service.ad;

import by.shimakser.keycloak.service.SecurityService;
import by.shimakser.model.ad.Advertiser;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.AdvertiserRepository;
import by.shimakser.repository.ad.UserRepository;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class AdvertiserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertiserRepository advertiserRepository;
    @Mock
    private SecurityService securityService;

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

    /**
     * {@link AdvertiserService#add(Advertiser)}
     */
    @Test
    void Given_CheckIsTitleBound_When_CreateAdvertiser_Then_CheckIsCorrectlyCreatedAdvertiser() {
        // given
        given(advertiserRepository.existsAdvertiserByAdvertiserTitle(ADVERTISER.getAdvertiserTitle())).willReturn(false);
        given(securityService.getPrincipalName()).willReturn(USER.getUsername());
        given(userRepository.findByUsername(USER.getUsername())).willReturn(Optional.of(USER));

        // when
        Advertiser advertiser = null;
        try {
            advertiser = advertiserService.add(ADVERTISER);
        } catch (EntityExistsException e) {
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

    /**
     * {@link AdvertiserService#add(Advertiser)}
     */
    @Test
    void Given_CheckIsTitleBound_When_CreateAdvertiser_Then_CatchExceptionByAlreadyBoundTitle() {
        // given
        given(advertiserRepository.existsAdvertiserByAdvertiserTitle(ADVERTISER.getAdvertiserTitle()))
                .willThrow(new EntityExistsException());

        // when
        BDDCatchException.when(() -> advertiserService.add(ADVERTISER));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityExistsException.class);
        then(advertiserRepository)
                .should(never())
                .save(ADVERTISER);
    }

    /**
     * {@link AdvertiserService#get(Long)}
     */
    @Test
    void Given_SearchAdvertiserById_When_GetAdvertiser_Then_CheckIsCorrectlySearchedAdvertiser() {
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

    /**
     * {@link AdvertiserService#get(Long)}
     */
    @Test
    void Given_SearchAdvertiserById_When_GetAdvertiser_Then_CatchExceptionByNotExistAdvertiser() {
        // given
        given(advertiserRepository.findById(ADVERTISER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> advertiserService.get(ADVERTISER_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * {@link AdvertiserService#getAll(Optional, Optional, Optional)}
     */
    @Test
    void Given_SearchAllAdvertisers_When_GetAdvertisers_Then_CheckIsCorrectlySearchedAdvertisers() {
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

    /**
     * {@link AdvertiserService#update(Long, Advertiser)}
     */
    @Test
    void Given_SearchAdvertiserById_When_UpdateAdvertiser_Then_CheckIsCorrectlyUpdatedAdvertiser() {
        // given
        Advertiser testAdvertiser = new Advertiser();
        testAdvertiser.setAdvertiserTitle("test");
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.of(ADVERTISER));
        given(securityService.checkPrincipalAccess(USER.getUsername())).willReturn(true);

        // when
        Advertiser updatedAdvertiser = null;
        try {
            updatedAdvertiser = advertiserService.update(ADVERTISER_ID, testAdvertiser);
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

    /**
     * {@link AdvertiserService#update(Long, Advertiser)}
     */
    @Test
    void Given_SearchAdvertiserByIdAnd_When_UpdateAdvertiser_Then_CatchExceptionByNotExistAdvertiser() {
        // given
        Advertiser testAdvertiser = new Advertiser();
        given(advertiserRepository.findById(ADVERTISER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> advertiserService.update(ADVERTISER_ID, ADVERTISER));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
        then(advertiserRepository)
                .should(never())
                .save(ADVERTISER);
    }

    /**
     * {@link AdvertiserService#delete(Long)}
     */
    @Test
    void Given_SearchAdvertiserById_When_DeleteAdvertiser_Then_CheckIsCorrectlyDeletedAdvertiser() {
        // given
        given(advertiserRepository.findById(ADVERTISER_ID)).willReturn(Optional.of(ADVERTISER));
        given(securityService.checkPrincipalAccess(USER.getUsername())).willReturn(true);

        // when
        try {
            advertiserService.delete(ADVERTISER_ID);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(advertiserRepository)
                .should()
                .save(ADVERTISER);
        assertEquals(Boolean.TRUE, ADVERTISER.isAdvertiserDeleted());
    }

    /**
     * {@link AdvertiserService#delete(Long)}
     */
    @Test
    void Given_SearchAdvertiserById_When_DeleteAdvertiser_Then_CatchExceptionByNotExistAdvertiser() {
        // given
        given(advertiserRepository.findById(ADVERTISER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> advertiserService.delete(ADVERTISER_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
        then(advertiserRepository)
                .should(never())
                .save(ADVERTISER);
        assertEquals(Boolean.FALSE, ADVERTISER.isAdvertiserDeleted());
    }

    /**
     * {@link AdvertiserService#getDeletedAdvertiser(Long)}
     */
    @Test
    void Given_SearchDeletedAdvertiserById_When_GetDeletedAdvertiser_Then_CheckIsCorrectlySearchedAdvertiser() {
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

    /**
     * {@link AdvertiserService#getDeletedAdvertiser(Long)}
     */
    @Test
    void Given_SearchDeletedAdvertiserById_When_GetDeletedAdvertiser_Then_CatchExceptionByNotExistAdvertiser() {
        // given
        given(advertiserRepository.findByIdAndAdvertiserDeletedTrue(ADVERTISER_ID))
                .willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> advertiserService.getDeletedAdvertiser(ADVERTISER_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * {@link AdvertiserService#getDeletedAdvertisers()}
     */
    @Test
    void Given_SearchAllDeletedAdvertisers_When_GetDeletedAdvertisers_Then_CheckIsCorrectlySearchedAdvertisers() {
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