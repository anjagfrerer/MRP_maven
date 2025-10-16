package persistence;

import model.MediaEntry;
import model.Rating;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RatingRepository implements IRatingRepository {
    private List<Rating> ratings;
    private static RatingRepository instance = new RatingRepository();

    public RatingRepository() {
        ratings = new ArrayList<>();
    }

    public static RatingRepository getInstance() {
        return instance;
    }

    @Override
    public void likeRating(User user, Rating rating) {
        rating.getLikes().add(user);
    }

    @Override
    public void unlikeRating(User user, Rating rating) {
        rating.getLikes().remove(user);
    }

    @Override
    public void editRating(Rating rating, int stars, String comment) {
        rating.setStars(stars);
        rating.setComment(comment);
    }

    @Override
    public void deleteRating(Rating rating) {
        ratings.remove(rating);
    }

    @Override
    public List<Rating> getRatings() {
        return ratings;
    }

    @Override
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public Rating getRatingByID(String id) {
        for (Rating rating : ratings) {
            if (rating.getId().equals(id)) {
                return rating;
            }
        }
        return null; // falls kein Rating gefunden wurde
    }

    @Override
    public void createRating(User user, int stars, String comment, MediaEntry mediaEntry) {
        Rating rating = new Rating(UUID.randomUUID().toString(), mediaEntry, stars, comment, user);
        rating.setLocalDate(LocalDateTime.now());
        ratings.add(rating);
    }
}
