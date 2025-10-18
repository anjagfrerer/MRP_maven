package persistence;

import model.MediaEntry;
import model.Rating;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class that manages all ratings and provides methods
 * to create, edit, delete, and like ratings.
 */
public class RatingRepository implements IRatingRepository {
    private List<Rating> ratings;
    private static RatingRepository instance = new RatingRepository();

    /** Private constructor to prevent creating multiple instances. */
    private RatingRepository() {
        ratings = new ArrayList<>();
    }

    /**
     * Returns the single instance of this repository.
     *
     * @return the shared RatingRepository instance
     */
    public static RatingRepository getInstance() {
        return instance;
    }

    /**
     * Adds a like from a user to a rating.
     *
     * @param user the user who liked the rating
     * @param rating the rating being liked
     */
    @Override
    public void likeRating(User user, Rating rating) {
        rating.getLikes().add(user);
    }

    /**
     * Removes a like from a user on a rating.
     *
     * @param user the user who unliked the rating
     * @param rating the rating being unliked
     */
    @Override
    public void unlikeRating(User user, Rating rating) {
        rating.getLikes().remove(user);
    }

    /**
     * Edits an existing rating.
     *
     * @param rating the rating to edit
     * @param stars new number of stars
     * @param comment new comment text
     */
    @Override
    public void editRating(Rating rating, int stars, String comment) {
        rating.setStars(stars);
        rating.setComment(comment);
    }

    /**
     * Deletes a rating from the list.
     *
     * @param rating the rating to remove
     */
    @Override
    public void deleteRating(Rating rating) {
        ratings.remove(rating);
    }

    /**
     * Returns all ratings.
     *
     * @return list of all ratings
     */
    @Override
    public List<Rating> getRatings() {
        return ratings;
    }

    /**
     * Replaces the current list of ratings with a new one.
     *
     * @param ratings new list of ratings
     */
    @Override
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * Finds a rating by its ID.
     *
     * @param id the ID of the rating
     * @return the found rating
     */
    @Override
    public Rating getRatingByID(String id) {
        for (Rating rating : ratings) {
            if (rating.getId().equals(id)) {
                return rating;
            }
        }
        return null; // falls kein Rating gefunden wurde
    }

    /**
     * Creates a new rating and adds it to the list.
     * Generates a random unique ID and sets the current date.
     *
     * @param user the creator of the rating
     * @param stars number of stars
     * @param comment optional comment
     * @param mediaEntry the media being rated
     */
    @Override
    public void createRating(User user, int stars, String comment, MediaEntry mediaEntry) {
        Rating rating = new Rating(UUID.randomUUID().toString(), mediaEntry, stars, comment, user);
        rating.setLocalDate(LocalDateTime.now());
        ratings.add(rating);
    }
}
