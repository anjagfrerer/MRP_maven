package service;

import model.MediaEntry;
import model.User;

public interface IRatingService {
    void likeRating(User user, String ratingID);
    void unlikeRating(User user, String ratingID);
    void editRating(String ratingID, int stars, String comment);
    void deleteRating(String ratingID);
    void addRating(User user, int stars, String comment, MediaEntry mediaEntry);
}
