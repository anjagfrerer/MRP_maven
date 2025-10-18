package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.MediaEntry;
import model.User;
import service.IRatingService;
import service.RatingService;

import java.io.IOException;

/**
 * RatingHandler is responsible for handling all HTTP requests related to ratings.
 * For now, it provides methods to add, edit, like, unlike, and delete ratings.
 * In the future, all HTTP requests (GET, POST, PUT, DELETE)
 * will be handled inside the handle() method.
 * The requests will then be passed to a new RatingController
 * which will take care of the main business logic.
 */

public class RatingHandler implements HttpHandler {
    private final IRatingService ratingService;

    public RatingHandler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Adds a like to a rating.
     * This logic will later be moved to the RatingController,
     * while this handler will only forward the HTTP request.
     *
     * @param user the user who likes the rating
     * @param ratingID the ID of the rating to like
     */
    public void likeRating(User user, String ratingID) {
        this.ratingService.likeRating(user, ratingID);
    }

    /**
     * Adds a new rating for a media entry. This will later be done by the RatingController.
     *
     * @param user the user who creates the rating
     * @param stars the number of stars given
     * @param comment an optional comment
     * @param mediaEntry the media entry being rated
     */
    public void addRating(User user, int stars, String comment, MediaEntry mediaEntry) {
        if(user != null && mediaEntry != null){
            this.ratingService.addRating(user, stars, comment, mediaEntry);
        }
    }

    /**
     * Removes a like from a rating. This will later be handled through the RatingController.
     *
     * @param user the user who removes the like
     * @param ratingID the ID of the rating
     */
    public void unlikeRating(User user, String ratingID) {
        this.ratingService.unlikeRating(user, ratingID);
    }

    /**
     * Edits an existing rating if valid data is provided. Later, this check and logic will be moved to the RatingController.
     *
     * @param mediaEntryID the ID of the related media entry
     * @param stars the new star value
     * @param comment the new comment
     * @param creator the user who created the rating
     * @param ratingID the ID of the rating to edit
     */
    void editRating(String mediaEntryID, int stars, String comment, User creator, String ratingID) {
        if (!mediaEntryID.isBlank() && creator != null && !ratingID.isBlank()) {
            // Hier noch checken, ob der User auch wirklich der Creator ist
            this.ratingService.editRating(ratingID, stars, comment);
        }
    }

    /**
     * Deletes a rating created by a user. Later, this check and logic will be done by the RatingController.
     *
     * @param user the user who created the rating
     * @param mediaEntryID the ID of the media entry
     */
    void deleteRating(User user, String mediaEntryID) {
        if(user != null && !mediaEntryID.isBlank()) {
            // Hier noch checken, ob der User auch wirklich der Creator ist
            this.ratingService.deleteRating(mediaEntryID);
        }
    }

    /**
     * Handles all incoming HTTP requests for ratings. In the future, this method will:
     *
     * Check the HTTP method (GET, POST, PUT, DELETE)
     * Read request data and headers
     * Forward the request to the RatingController
     * Send the controller’s response back to the client
     *
     * @param exchange the HTTP exchange containing the request and response
     * @throws IOException if an input/output error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
