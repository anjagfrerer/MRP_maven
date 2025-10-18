package service;

import model.MediaEntry;
import model.Rating;
import model.User;
import persistence.IRatingRepository;


/**
 * Service class for managing ratings of media entries.
 * Provides methods to add, edit, delete, like, and unlike ratings.
 */
public class RatingService implements IRatingService {
    private static RatingService instance;
    private IRatingRepository ratingRepository;

    /**
     * Private constructor for singleton pattern.
     *
     * @param ratingRepository the repository used to store ratings
     */
    private RatingService(IRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Returns the single instance of RatingService.
     *
     * @param ratingRepository the repository used to store ratings
     * @return the instance
     */
    public static RatingService getInstance(IRatingRepository ratingRepository) {
        if (instance == null) instance = new RatingService(ratingRepository);
        return instance;
    }

    /**
     * Adds a like from a user to a rating.
     *
     * @param user the user who likes the rating
     * @param ratingID the ID of the rating to like
     */
    @Override
    public void likeRating(User user, String ratingID) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.likeRating(user, rating);
        }
    }

    /**
     * Removes a like from a user on a rating.
     *
     * @param user the user who unlikes the rating
     * @param ratingID the ID of the rating to unlike
     */
    @Override
    public void unlikeRating(User user, String ratingID) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.unlikeRating(user, rating);
        }
    }

    /**
     * Edits an existing ratings stars and comment.
     *
     * @param ratingID the ID of the rating to edit
     * @param stars new number of stars
     * @param comment new text
     */
    @Override
    public void editRating(String ratingID, int stars, String comment) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.editRating(rating, stars, comment);
        }
    }

    /**
     * Deletes a rating by its ID.
     *
     * @param ratingID the ID of the rating to delete
     */
    @Override
    public void deleteRating(String ratingID) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.deleteRating(rating);
        }
    }

    /**
     * Adds a new rating for a media entry.
     *
     * @param user the creator of the rating
     * @param stars number of stars
     * @param comment optional comment
     * @param mediaEntry the media entry being rated
     */
    @Override
    public void addRating(User user, int stars, String comment, MediaEntry mediaEntry) {
        this.ratingRepository.createRating(user, stars, comment, mediaEntry);
    }
}