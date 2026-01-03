package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
     * creates an RatingController with a corresponding IRatingService, that is responsible for the registration and login logic.
     * @param ratingService
     * @return
     */
    public RatingController(IRatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Returns a single instance of the UserController (Singleton pattern). If no instance exists, a new one is created.
     * @param ratingService
     * @return
     */
    public static RatingController getInstance(IRatingService ratingService) {
        if (instance == null) instance = new RatingController(ratingService);
        return instance;
    }

    public Response likeRating(int ratingid, User user) {
        try {
            boolean success = ratingService.likeRating(ratingid, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Like successful"))
                );
            }else{
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Like was not successful"))
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

    public Response rateMediaEntry(int mediaentryid, String requestBody, User user) {
        try {
            Rating rating = this.getObjectMapper().readValue(requestBody, Rating.class);
            boolean success = ratingService.rateMediaEntry(mediaentryid, rating.getStars(), rating.getComment(), user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry successfully rated"))
                );
            }else{
                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Rating was not successful"))
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

    public Response updateRating(int mediaentryid, String requestBody, User user) {
        try {
            Rating rating = this.getObjectMapper().readValue(requestBody, Rating.class);
            boolean success = ratingService.updateRating(mediaentryid, rating.getStars(), rating.getComment(), user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Rating successfully updated"))
                );
            }else{
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Update was not successful"))
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

    public Response getRatingHistory(int userId, User user) {
        try {
            List<Rating> ratings = ratingService.getRatingHistory(userId, user);

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
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response confirmRatingComment(int ratingid, User user) {
        try {
            boolean success = ratingService.confirmRatingComment(ratingid, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Rating comment confirmed"))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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