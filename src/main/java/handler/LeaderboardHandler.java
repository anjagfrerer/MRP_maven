package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.LeaderboardController;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.http.Method;
import restserver.server.Request;
import restserver.server.Response;
import service.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handles HTTP requests for the leaderboard.
 * It forwards requests to the LeaderboardController.
 */
public class LeaderboardHandler implements HttpHandler {
    private final ILeaderboardService leaderboardService;
    private LeaderboardController leaderboardController;

    /**
     * Creates a new LeaderboardHandler.
     *
     * @param leaderboardService service used to get leaderboard data
     */
    public LeaderboardHandler(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
        this.leaderboardController = LeaderboardController.getInstance(leaderboardService);
    }

    /**
     * Handles incoming HTTP requests.
     * Only getLeaderboard is supported.
     *
     * @param httpExchange contains the HTTP request and response
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        Response response;

        if (httpExchange.getRequestMethod().equals(Method.GET.name())) {
            response = leaderboardController.getLeaderboard();
        } else {
            response = new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"error\": \"Internal Server Error.\" }"
            );
        }
        response.send(httpExchange);
    }
}