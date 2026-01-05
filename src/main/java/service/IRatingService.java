package service;

import model.MediaEntry;
import model.Rating;
import model.User;
import dto.RatingHistoryDTO;
import java.util.List;

/**
 * Service interface for managing ratings.
 * Provides methods to add, edit, delete, like, and unlike ratings.
 */
public interface IRatingService {
    boolean likeRating(int ratingid, User user);
    boolean rateMediaEntry(int mediaentryid, int stars, String comment, User user);
    boolean updateRating(int ratingid, int stars, String comment, User user);
    List<RatingHistoryDTO> getRatingHistory(int userId, User user);
    boolean confirmRatingComment(int ratingid, User user);
    boolean deleteRating(int ratingid, User user);
}