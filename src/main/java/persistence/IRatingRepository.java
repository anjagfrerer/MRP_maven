package persistence;

import dto.RatingHistoryDTO;
import model.MediaEntry;
import model.Profile;
import model.Rating;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for managing ratings in the repository.
 * Provides methods to add, update, delete, like, and fetch ratings and rating history.
 */
public interface IRatingRepository {
    boolean likeRating(int ratingid, User user);
    boolean rateMediaEntry(int mediaentryid, int stars, String comment, User user);
    boolean updateRating(int mediaentryid, int stars, String comment, User user);
    List<RatingHistoryDTO> getRatingHistory(int userId, User user);
    Rating getRatingById(int ratingid);
    boolean confirmRatingComment(int ratingid);
    boolean deleteRating(int ratingid);
    boolean hasUserLikedRating(int ratingId, int userId);
}
