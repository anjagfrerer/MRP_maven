package persistence;

import model.MediaEntry;
import model.Rating;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for managing ratings in the repository.
 * Provides methods to create, edit, delete, and like ratings.
 */
public interface IRatingRepository {
    boolean likeRating(int ratingid, User user);
    boolean rateMediaEntry(int mediaentryid, int stars, String comment, User user);
    boolean updateRating(int mediaentryid, int stars, String comment, User user);
    List<Rating> getRatingHistory(int userId, User user);
    Rating getRatingById(int ratingid);
    boolean confirmRatingComment(int ratingid);
}
