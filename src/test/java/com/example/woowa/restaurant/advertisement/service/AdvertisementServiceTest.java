package com.example.woowa.restaurant.advertisement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementCreateRequest;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementUpdateRequest;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementCreateResponse;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementFindResponse;
import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.advertisement.mapper.AdvertisementMapper;
import com.example.woowa.restaurant.advertisement.repository.AdvertisementRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisement;
import com.example.woowa.restaurant.restaurant_advertisement.repository.RestaurantAdvertisementRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AdvertisementServiceTest {

    @Autowired
    private RestaurantAdvertisementRepository restaurantAdvertisementRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    private AdvertisementMapper advertisementMapper = Mappers.getMapper(AdvertisementMapper.class);

    @Test
    @DisplayName("????????? ????????? ????????????.")
    void testCreateAdvertisement() {
        // Given
        AdvertisementRepository mockedAdvertisementRepository = mock(AdvertisementRepository.class);
        AdvertisementService advertisementService = withMockedAdvertisementRepository(mockedAdvertisementRepository);

        AdvertisementCreateRequest advertisementCreateRequest =
            new AdvertisementCreateRequest("????????????", UnitType.MOTHLY.getType(), RateType.FLAT.getType(),
                88000, "???????????? ??????", 10);
        AdvertisementCreateResponse advertisementCreateResponse =
            new AdvertisementCreateResponse(1L, "????????????", UnitType.MOTHLY.getType(), RateType.FLAT.getType(),
                88000, "???????????? ??????", 10, 0, LocalDateTime.now());
        Advertisement entityConvertedFromDto = new Advertisement("????????????", UnitType.MOTHLY, RateType.FLAT,
            88000, "???????????? ??????", 10);
        when(mockedAdvertisementRepository.save(any(Advertisement.class)))
            .thenReturn(new Advertisement("????????????", UnitType.MOTHLY, RateType.FLAT, 88000, "???????????? ??????", 10));

        // When
        AdvertisementCreateResponse result = advertisementService.createAdvertisement(
            advertisementCreateRequest);

        // Then
        ArgumentCaptor<Advertisement> entityCaptor = ArgumentCaptor.forClass(
            Advertisement.class);
        verify(mockedAdvertisementRepository, times(1)).save(entityCaptor.capture());

        assertThat(entityConvertedFromDto.getTitle()).isEqualTo(entityCaptor.getValue().getTitle());
        assertThat(result.getTitle()).isEqualTo(advertisementCreateResponse.getTitle());
    }

    @Test
    @DisplayName("?????? ????????? ????????????.")
    void testFindAdvertisements() {
        // Given
        AdvertisementRepository mockedAdvertisementRepository = mock(AdvertisementRepository.class);
        AdvertisementService advertisementService = withMockedAdvertisementRepository(mockedAdvertisementRepository);

        List<Advertisement> testers = makeAdvertisements(2);
        List<AdvertisementFindResponse> manualConversion = testers.stream()
            .map(ad -> new AdvertisementFindResponse(ad.getId(), ad.getTitle(), ad.getUnitType().getType(), ad.getRateType().getType(),
                ad.getRate(), ad.getDescription(), ad.getLimitSize(), ad.getCurrentSize(), ad.getCreatedAt(), ad.getUpdatedAt()))
            .collect(Collectors.toList());
        when(mockedAdvertisementRepository.findAll()).thenReturn(testers);

        // When
        List<AdvertisementFindResponse> result = advertisementService.findAdvertisements();

        // Then
        verify(mockedAdvertisementRepository, times(1)).findAll();
        for (int i = 0; i < result.size(); i++)
            assertThat(result).usingRecursiveComparison().isEqualTo(manualConversion);
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ????????? ????????????.")
    void testFindAdvertisementById() {
        // Given
        AdvertisementRepository mockedAdvertisementRepository = mock(AdvertisementRepository.class);
        AdvertisementService advertisementService = withMockedAdvertisementRepository(mockedAdvertisementRepository);

        Advertisement tester = new Advertisement("????????????", UnitType.MOTHLY, RateType.FLAT,
            88000, "???????????? ??????", 10);
        AdvertisementFindResponse manualConversion = new AdvertisementFindResponse(
            tester.getId(), tester.getTitle(), tester.getUnitType().getType(),
            tester.getRateType().getType(),
            tester.getRate(), tester.getDescription(), tester.getLimitSize(),
            tester.getCurrentSize(), tester.getCreatedAt(), tester.getUpdatedAt());
        when(mockedAdvertisementRepository.findById(1L)).thenReturn(Optional.of(tester));

        // When
        AdvertisementFindResponse result = advertisementService.findAdvertisementById(1L);

        // Then
        verify(mockedAdvertisementRepository, times(1)).findById(1L);
        assertThat(result.getTitle()).isEqualTo(manualConversion.getTitle());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ?????? ????????????.")
    void testUpdateAdvertisementById() {
        // Given
        AdvertisementService advertisementService =
            new AdvertisementService(restaurantAdvertisementRepository, restaurantRepository,
                advertisementRepository, advertisementMapper);
        AdvertisementCreateResponse beforeUpdating = advertisementService.createAdvertisement(
            new AdvertisementCreateRequest("????????????", UnitType.MOTHLY.getType(),
                RateType.FLAT.getType(), 88000, "???????????? ??????", 10));

        // When
        AdvertisementUpdateRequest advertisementUpdateRequest = new AdvertisementUpdateRequest("???????????????",
            UnitType.MOTHLY.getType(), RateType.PERCENT.getType(),
            10, "?????????????????????.");
        advertisementService.updateAdvertisementById(beforeUpdating.getId(), advertisementUpdateRequest);
        AdvertisementFindResponse afterUpdating = advertisementService.findAdvertisementById(beforeUpdating.getId());

        // Then
        assertThat(afterUpdating.getId()).isEqualTo(beforeUpdating.getId());
        assertThat(afterUpdating.getTitle()).isEqualTo(advertisementUpdateRequest.getTitle());
    }

    @Test
    @DisplayName("????????? ????????? ????????????.")
    void testIncludeRestaurantInAdvertisement() {
        // Given
        Restaurant restaurant = restaurantRepository.save(
            Restaurant.createRestaurant("????????????", "760-15-00993", LocalTime.now(),
                LocalTime.now().plusHours(1), false, "010-1010-1010", "????????????", "????????? ?????????"));
        Advertisement advertisement = advertisementRepository.save(new Advertisement("????????????", UnitType.MOTHLY, RateType.FLAT,
            88000, "???????????? ??????", 10));
        AdvertisementService advertisementService =
            new AdvertisementService(restaurantAdvertisementRepository, restaurantRepository,
                advertisementRepository, advertisementMapper);

        // When
        advertisementService.includeRestaurantInAdvertisement(advertisement.getId(), restaurant.getId());

        // Then
        AdvertisementFindResponse advertisementFindResponse = advertisementService.findAdvertisementById(
            advertisement.getId());

        assertThat(advertisementFindResponse.getCurrentSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("???????????? ????????? ????????????.")
    void testExcludeRestaurantOutOfAdvertisement() {
        // Given
        Restaurant restaurant = restaurantRepository.save(
            Restaurant.createRestaurant("????????????", "760-15-00993", LocalTime.now(),
                LocalTime.now().plusHours(1), false, "010-1010-1010", "????????????", "????????? ?????????"));
        Advertisement advertisement = advertisementRepository.save(new Advertisement("????????????", UnitType.MOTHLY, RateType.FLAT,
            88000, "???????????? ??????", 10));
        restaurantAdvertisementRepository.save(new RestaurantAdvertisement(restaurant, advertisement));
        AdvertisementService advertisementService =
            new AdvertisementService(restaurantAdvertisementRepository, restaurantRepository,
                advertisementRepository, advertisementMapper);

        AdvertisementFindResponse beforeExclusion = advertisementService.findAdvertisementById(
            advertisement.getId());
        assertThat(beforeExclusion.getCurrentSize()).isEqualTo(1);

        // When
        advertisementService.excludeRestaurantOutOfAdvertisement(advertisement.getId(), restaurant.getId());

        // Then
        AdvertisementFindResponse afterExclusion = advertisementService.findAdvertisementById(
            advertisement.getId());
        assertThat(afterExclusion.getCurrentSize()).isEqualTo(0);
    }

    public AdvertisementService withMockedAdvertisementRepository(AdvertisementRepository mockedAdvertisementRepository) {
        return new AdvertisementService(restaurantAdvertisementRepository, restaurantRepository,
            mockedAdvertisementRepository, advertisementMapper);
    }

    public List<Advertisement> makeAdvertisements(int n) {
        List<Advertisement> list = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            list.add(new Advertisement("title" + i, UnitType.MOTHLY, RateType.PERCENT, 10, "description" + i, i));
        return list;
    }

}