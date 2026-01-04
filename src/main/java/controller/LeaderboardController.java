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
import java.util.Collections;

public class LeaderboardController extends Controller{
    private static ILeaderboardService leaderboardService;
    private static LeaderboardController instance;

    /**
     * Creates a new LeaderboardController.
     *
     * @param leaderboardService the service used to get leaderboard data
     * @return no return value (constructor)
     */
    public LeaderboardController(ILeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * Returns the single instance of the LeaderboardController.
     * If it does not exist, it will be created.
     *
     * @param leaderboardService the service used to get leaderboard data
     * @return the LeaderboardController instance
     */
    public static LeaderboardController getInstance(ILeaderboardService leaderboardService) {
        if (instance == null) instance = new LeaderboardController(leaderboardService);
        return instance;
    }

    /**
     * Gets the leaderboard as a list of profiles.
     *
     * @return HTTP response with the leaderboard in JSON format
     */
    public Response getLeaderboard() {
        try {
            List<Profile> leaderboard = leaderboardService.getLeaderboard();
            if (leaderboard == null) {
                leaderboard = Collections.emptyList();
            }
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    getObjectMapper().writeValueAsString(leaderboard)
            );
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