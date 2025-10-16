package service;

import model.MediaEntry;
import model.Rating;
import model.User;
import persistence.IRatingRepository;

public class RatingService implements IRatingService {
    private static RatingService instance;
    private IRatingRepository ratingRepository;

    private RatingService(IRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public static RatingService getInstance(IRatingRepository ratingRepository) {
        if (instance == null) instance = new RatingService(ratingRepository);
        return instance;
    }

    @Override
    public void likeRating(User user, String ratingID) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.likeRating(user, rating);
        }
    }

    @Override
    public void unlikeRating(User user, String ratingID) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.unlikeRating(user, rating);
        }
    }

    @Override
    public void editRating(String ratingID, int stars, String comment) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.editRating(rating, stars, comment);
        }
    }

    @Override
    public void deleteRating(String ratingID) {
        Rating rating = this.ratingRepository.getRatingByID(ratingID);
        if (rating != null) {
            this.ratingRepository.deleteRating(rating);
        }
    }

    @Override
    public void addRating(User user, int stars, String comment, MediaEntry mediaEntry) {
        this.ratingRepository.createRating(user, stars, comment, mediaEntry);
    }
}