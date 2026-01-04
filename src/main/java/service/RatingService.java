package service;

import dto.RatingHistoryDTO;
import model.User;
import persistence.IRatingRepository;

import java.util.List;


/**
 * Service class for managing ratings of media entries.
 * Provides methods to add, edit, delete, like, and confirm ratings.
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
     * @return the singleton instance
     */
    public static RatingService getInstance(IRatingRepository ratingRepository) {
        if (instance == null) instance = new RatingService(ratingRepository);
        return instance;
    }

    /**
     * Adds a like from a user to a rating.
     *
     * @param ratingId the ID of the rating to like
     * @param user the user who likes the rating
     * @return true if the like is added, false if user has already liked or user is null
     */
    @Override
    public boolean likeRating(int ratingId, User user) {
        if (user == null) return false;
        if (ratingRepository.hasUserLikedRating(ratingId, user.getUserid())) {
            return false;
        }
        return ratingRepository.likeRating(ratingId, user);
    }

    /**
     * Adds a new rating for a media entry.
     *
     * @param mediaentryid the ID of the media entry being rated
     * @param stars number of stars (1-5)
     * @param comment optional comment
     * @param user the creator of the rating
     * @return true if rating is added successfully, false otherwise
     */
    @Override
    public boolean rateMediaEntry(int mediaentryid, int stars, String comment, User user) {
        if(user == null || stars <= 0 || stars > 5) return false;
        return this.ratingRepository.rateMediaEntry(mediaentryid, stars, comment, user);
    }

    /**
     * Updates an existing rating.
     *
     * @param mediaentryid the ID of the media entry
     * @param stars updated number of stars (1-5)
     * @param comment updated comment
     * @param user the user performing the update
     * @return true if the update is successful, false otherwise
     */
    @Override
    public boolean updateRating(int mediaentryid, int stars, String comment, User user) {
        if(user == null || stars <= 0 || stars > 5) return false;
        return this.ratingRepository.updateRating(mediaentryid, stars, comment, user);
    }

    /**
     * Returns the rating history of a user.
     *
     * @param userId the ID of the user
     * @param user the user performing the request
     * @return list of RatingHistoryDTO or null if user is invalid
     */
    @Override
    public List<RatingHistoryDTO> getRatingHistory(int userId, User user) {
        if(user==null || userId != user.getUserid()) return null;
        return ratingRepository.getRatingHistory(userId, user);
    }

    /**
     * Confirms a rating comment
     *
     * @param ratingid the ID of the rating
     * @param user the user performing the confirmation
     * @return true if confirmation is successful, false otherwise
     */
    @Override
    public boolean confirmRatingComment(int ratingid, User user) {
        if(user==null) return false;
        if(ratingRepository.getRatingById(ratingid)==null) return false;
        if(ratingRepository.getRatingById(ratingid).getCreatorId() != user.getUserid()) return false;
        return ratingRepository.confirmRatingComment(ratingid);
    }

    /**
     * Deletes a rating.
     *
     * @param ratingid the ID of the rating to delete
     * @param user the user performing the deletion
     * @return true if deletion is successful, false otherwise
     */
    @Override
    public boolean deleteRating(int ratingid, User user) {
        if(user==null) return false;
        if(ratingRepository.getRatingById(ratingid)==null) return false;
        if(ratingRepository.getRatingById(ratingid).getCreatorId() != user.getUserid()) return false;
        return ratingRepository.deleteRating(ratingid);
    }

    /**
     * Resets the singleton instance (for testing purposes).
     */
    public static void resetInstance() {
        instance = null;
    }
}