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
    void updateRating_invalidStars_shouldFail() {
        assertFalse(ratingService.updateRating(1, 0, "Bad", user));
        assertFalse(ratingService.updateRating(1, 6, "Bad", user));
    }

    @Test
    void updateRating_nullUser_shouldFail() {
        assertFalse(ratingService.updateRating(1, 3, "Update", null));
    }

    @Test
    void deleteRating_creator_shouldSucceed() {
        Rating rating = new Rating();
        rating.setCreator(user.getUserid());
        when(ratingRepository.getRatingById(1)).thenReturn(rating);
        when(ratingRepository.deleteRating(1)).thenReturn(true);
        assertTrue(ratingService.deleteRating(1, user));
    }

    @Test
    void deleteRating_notCreator_shouldFail() {
        Rating rating = new Rating();
        rating.setCreator(2); // anderer User
        when(ratingRepository.getRatingById(1)).thenReturn(rating);
        assertFalse(ratingService.deleteRating(1, user));
    }

    @Test
    void deleteRating_nullUser_shouldFail() {
        assertFalse(ratingService.deleteRating(1, null));
    }

    @Test
    void deleteRating_nonExistingRating_shouldFail() {
        when(ratingRepository.getRatingById(1)).thenReturn(null);
        assertFalse(ratingService.deleteRating(1, user));
    }

    @Test
    void likeRating_valid_shouldReturnTrue() {
        when(ratingRepository.hasUserLikedRating(1, user.getUserid())).thenReturn(false);
        when(ratingRepository.likeRating(1, user)).thenReturn(true);
        assertTrue(ratingService.likeRating(1, user));
    }

    @Test
    void likeRating_alreadyLiked_shouldFail() {
        when(ratingRepository.hasUserLikedRating(1, user.getUserid())).thenReturn(true);
        assertFalse(ratingService.likeRating(1, user));
    }

    @Test
    void likeRating_nullUser_shouldFail() {
        assertFalse(ratingService.likeRating(1, null));
    }

    @Test
    void confirmRatingComment_creator_shouldSucceed() {
        Rating rating = new Rating();
        rating.setCreator(user.getUserid());
        when(ratingRepository.getRatingById(1)).thenReturn(rating);
        when(ratingRepository.confirmRatingComment(1)).thenReturn(true);
        assertTrue(ratingService.confirmRatingComment(1, user));
    }

    @Test
    void confirmRatingComment_notCreator_shouldFail() {
        Rating rating = new Rating();
        rating.setCreator(2); // anderer User
        when(ratingRepository.getRatingById(1)).thenReturn(rating);
        assertFalse(ratingService.confirmRatingComment(1, user));
    }

    @Test
    void confirmRatingComment_nullUser_shouldFail() {
        assertFalse(ratingService.confirmRatingComment(1, null));
    }

    @Test
    void confirmRatingComment_nonExistingRating_shouldFail() {
        when(ratingRepository.getRatingById(1)).thenReturn(null);
        assertFalse(ratingService.confirmRatingComment(1, user));
    }

    @Test
    void getRatingHistory_self_shouldReturnList() {
        when(ratingRepository.getRatingHistory(1, user)).thenReturn(List.of(new RatingHistoryDTO()));
        List<RatingHistoryDTO> result = ratingService.getRatingHistory(1, user);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getRatingHistory_otherUser_shouldFail() {
        User other = new User();
        other.setUserid(2);
        List<RatingHistoryDTO> result = ratingService.getRatingHistory(2, user);
        assertNull(result);
    }

    @Test
    void getRatingHistory_nullUser_shouldFail() {
        assertNull(ratingService.getRatingHistory(1, null));
    }
}
