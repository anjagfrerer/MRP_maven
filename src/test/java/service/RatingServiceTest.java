package service;

import dto.RatingHistoryDTO;
import model.Rating;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.IRatingRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private IRatingRepository ratingRepository;

    private RatingService ratingService;

    private User user;

    @BeforeEach
    void setup() {
        ratingService = RatingService.getInstance(ratingRepository);
        user = new User();
        user.setUserid(1);
    }

    @BeforeEach
    void resetSingleton() {
        RatingService.resetInstance();
    }

    @Test
    void rateMediaEntry_valid_shouldReturnTrue() {
        when(ratingRepository.rateMediaEntry(1, 5, "Great", user)).thenReturn(true);

        boolean result = ratingService.rateMediaEntry(1, 5, "Great", user);

        assertTrue(result);
    }

    @Test
    void rateMediaEntry_invalidStars_shouldFail() {
        boolean result = ratingService.rateMediaEntry(1, 6, "Bad", user);

        assertFalse(result);
    }

    @Test
    void rateMediaEntry_nullUser_shouldFail() {
        boolean result = ratingService.rateMediaEntry(1, 4, "Nice", null);

        assertFalse(result);
    }

    @Test
    void updateRating_byCreator_shouldSucceed() {
        when(ratingRepository.updateRating(1, 4, "Updated", user)).thenReturn(true);

        boolean result = ratingService.updateRating(1, 4, "Updated", user);

        assertTrue(result);
    }

    @Test
    void getRatingHistory_otherUser_shouldFail() {
        User other = new User();
        other.setUserid(2);

        List<RatingHistoryDTO> result = ratingService.getRatingHistory(2, user);

        assertNull(result);
    }

    @Test
    void likeRating_valid_shouldReturnTrue() {
        when(ratingRepository.likeRating(1, user)).thenReturn(true);

        boolean result = ratingService.likeRating(1, user);

        assertTrue(result);
    }

    @Test
    void confirmRatingComment_onlyCreatorAllowed() {
        Rating rating = new Rating();
        rating.setCreator(user.getUserid());

        when(ratingRepository.getRatingById(1)).thenReturn(rating);
        when(ratingRepository.confirmRatingComment(1)).thenReturn(true);

        boolean result = ratingService.confirmRatingComment(1, user);

        assertTrue(result);
    }
}
