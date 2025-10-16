package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.MediaEntry;
import model.User;
import service.IRatingService;
import service.RatingService;

import java.io.IOException;

public class RatingHandler implements HttpHandler {
    private final IRatingService ratingService;

    public RatingHandler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public void likeRating(User user, String ratingID) {
        this.ratingService.likeRating(user, ratingID);
    }

    public void addRating(User user, int stars, String comment, MediaEntry mediaEntry) {
        if(user != null && mediaEntry != null){
            this.ratingService.addRating(user, stars, comment, mediaEntry);
        }
    }

    public void unlikeRating(User user, String ratingID) {
        this.ratingService.unlikeRating(user, ratingID);
    }

    void editRating(String mediaEntryID, int stars, String comment, User creator, String ratingID) {
        if (!mediaEntryID.isBlank() && creator != null && !ratingID.isBlank()) {
            // Hier noch checken, ob der User auch wirklich der Creator ist
            this.ratingService.editRating(ratingID, stars, comment);
        }
    }
    void deleteRating(User user, String mediaEntryID) {
        if(user != null && !mediaEntryID.isBlank()) {
            // Hier noch checken, ob der User auch wirklich der Creator ist
            this.ratingService.deleteRating(mediaEntryID);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
