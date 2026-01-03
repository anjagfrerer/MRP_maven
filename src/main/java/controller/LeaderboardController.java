package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Profile;
import model.Rating;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.server.Response;
import service.ILeaderboardService;
import service.IRatingService;

import java.util.List;
import java.util.Map;

public class LeaderboardController extends Controller{
    private static ILeaderboardService leaderboardService;
    private static LeaderboardController instance;

    /**
     * creates an RatingController with a corresponding IRatingService, that is responsible for the registration and login logic.
     * @param ratingService
     * @return
     */
    public LeaderboardController(ILeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * Returns a single instance of the UserController (Singleton pattern). If no instance exists, a new one is created.
     * @param ratingService
     * @return
     */
    public static LeaderboardController getInstance(ILeaderboardService leaderboardService) {
        if (instance == null) instance = new LeaderboardController(leaderboardService);
        return instance;
    }

    public Response getLeaderboard() {
        try {
            List<Profile> leaderboard = leaderboardService.getLeaderboard();

            if(leaderboard!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(leaderboard)
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