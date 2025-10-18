package persistence;

import model.MediaEntry;
import model.Rating;
import model.User;

import java.util.List;

/**
 * Interface for managing ratings in the repository.
 * Provides methods to create, edit, delete, and like ratings.
 */
public interface IRatingRepository {
    void likeRating(User user, Rating rating);
    void unlikeRating(User user, Rating rating);
    void editRating(Rating rating, int stars, String comment);
    void deleteRating(Rating rating);
    List<Rating> getRatings();
    void setRatings(List<Rating> ratings);
    Rating getRatingByID(String id);
    void createRating(User user, int stars, String comment, MediaEntry mediaEntry);
}
