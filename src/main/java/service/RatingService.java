package service;

import dto.RatingHistoryDTO;
import model.Rating;
import model.User;
import persistence.IRatingRepository;
import persistence.RatingRepository;

import java.util.List;


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
    public boolean likeRating(int ratingid, User user) {
        if(user == null) return false;
        return this.ratingRepository.likeRating(ratingid, user);
    }

    /**
     * Removes a like from a user on a rating.
     *
     * @param user the user who unlikes the rating
     * @param ratingID the ID of the rating to unlike
     */

    /**
     * Deletes a rating by its ID.
     *
     * @param ratingID the ID of the rating to delete
     */

    /**
     * Adds a new rating for a media entry.
     *
     * @param user the creator of the rating
     * @param stars number of stars
     * @param comment optional comment
     * @param mediaEntry the media entry being rated
     */
    @Override
    public boolean rateMediaEntry(int mediaentryid, int stars, String comment, User user) {
        if(user == null || stars <= 0 || stars > 5 || comment.isEmpty()) return false;
        return this.ratingRepository.rateMediaEntry(mediaentryid, stars, comment, user);
    }

    @Override
    public boolean updateRating(int mediaentryid, int stars, String comment, User user) {
        if(user == null || stars <= 0 || stars > 5 || comment.isEmpty()) return false;
        return this.ratingRepository.updateRating(mediaentryid, stars, comment, user);
    }

    @Override
    public List<RatingHistoryDTO> getRatingHistory(int userId, User user) {
        if(user==null || userId != user.getUserid()) return null;
        return ratingRepository.getRatingHistory(userId, user);
    }

    @Override
    public boolean confirmRatingComment(int ratingid, User user) {
        if(user==null) return false;
        if(ratingRepository.getRatingById(ratingid)==null) return false;
        if(ratingRepository.getRatingById(ratingid).getCreatorId() != user.getUserid()) return false;
        return ratingRepository.confirmRatingComment(ratingid);
    }
}