package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dto.RatingHistoryDTO;
import model.Rating;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.server.Response;
import service.IRatingService;

import java.util.List;
import java.util.Map;

public class RatingController extends Controller{
    private static IRatingService ratingService;
    private static RatingController instance;

    /**
     * Creates a new RatingController.
     *
     * @param ratingService service used for rating logic
     * @return no return value (constructor)
     */
    public RatingController(IRatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Returns the single instance of RatingController.
     * Creates a new one if it does not exist.
     *
     * @param ratingService service used for rating logic
     * @return RatingController instance
     */
    public static RatingController getInstance(IRatingService ratingService) {
        if (instance == null) instance = new RatingController(ratingService);
        return instance;
    }

    /**
     * Likes a rating.
     *
     * @param ratingid ID of the rating
     * @param user the user who likes the rating
     * @return HTTP response with success or error message
     */
    public Response likeRating(int ratingid, User user) {
        try {
            boolean success = ratingService.likeRating(ratingid, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Like successful."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Like was not successful."))
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    /**
     * Rates a media entry.
     *
     * @param mediaentryid ID of the media entry
     * @param requestBody JSON data with rating information
     * @param user the user who rates the media entry
     * @return HTTP response with success or error message
     */
    public Response rateMediaEntry(int mediaentryid, String requestBody, User user) {
        try {
            Rating rating = this.getObjectMapper().readValue(requestBody, Rating.class);
            boolean success = ratingService.rateMediaEntry(mediaentryid, rating.getStars(), rating.getComment(), user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry successfully rated."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Rating was not successful."))
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    /**
     * Updates an existing rating.
     *
     * @param mediaentryid ID of the media entry
     * @param requestBody JSON data with updated rating
     * @param user the user who updates the rating
     * @return HTTP response with success or error message
     */
    public Response updateRating(int mediaentryid, String requestBody, User user) {
        try {
            Rating rating = this.getObjectMapper().readValue(requestBody, Rating.class);
            boolean success = ratingService.updateRating(mediaentryid, rating.getStars(), rating.getComment(), user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Rating successfully updated."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Update was not successful."))
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    /**
     * Gets the rating history of a user.
     *
     * @param userId ID of the user
     * @param user the requesting user
     * @return HTTP response with a list of rating history entries
     */
    public Response getRatingHistory(int userId, User user) {
        try {
            List<RatingHistoryDTO> ratings = ratingService.getRatingHistory(userId, user);

            if(ratings!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(ratings)
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Error loading Rating History."))
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    /**
     * Confirms a rating comment.
     *
     * @param ratingid ID of the rating
     * @param user the user who confirms the comment
     * @return HTTP response with success or error message
     */
    public Response confirmRatingComment(int ratingid, User user) {
        try {
            boolean success = ratingService.confirmRatingComment(ratingid, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Rating comment confirmed."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Rating comment could not be confirmed."))
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    /**
     * Deletes a rating.
     *
     * @param ratingid ID of the rating
     * @param user the user who deletes the rating
     * @return HTTP response with success or error message
     */
    public Response deleteRating(int ratingid, User user) {
        try {
            boolean success = ratingService.deleteRating(ratingid, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Rating deleted."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Rating could not be deleted."))
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
}