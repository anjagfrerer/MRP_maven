package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.LeaderboardController;
import controller.RatingController;
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
 * RatingHandler is responsible for handling all HTTP requests related to ratings.
 * For now, it provides methods to add, edit, like, unlike, and delete ratings.
 * In the future, all HTTP requests (GET, POST, PUT, DELETE)
 * will be handled inside the handle() method.
 * The requests will then be passed to a new RatingController
 * which will take care of the main business logic.
 */

public class LeaderboardHandler implements HttpHandler {
    private final ILeaderboardService leaderboardService;
    private LeaderboardController leaderboardController;

    public LeaderboardHandler(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
        this.leaderboardController = LeaderboardController.getInstance(leaderboardService);
    }

    /**
     * Handles all incoming HTTP requests for ratings. In the future, this method will:
     *
     * Check the HTTP method (GET, POST, PUT, DELETE)
     * Read request data and headers
     * Forward the request to the RatingController
     * Send the controller’s response back to the client
     *
     * @param httpExchange the HTTP exchange containing the request and response
     * @throws IOException if an input/output error occurs
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        try{
            Request request = new Request(httpExchange.getRequestURI());
            Response response = null;
            String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String authHeader = httpExchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response = new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON,
                        "{ \"error\": \"Missing or invalid token\" }");
                response.send(httpExchange);
                return;
            }

            String token = authHeader.substring("Bearer ".length());
            User user = UserService.getInstance(null).getUserByToken(token); // Singleton aus UserService verwenden

            if (user == null) {
                response = new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON,
                        "{ \"error\": \"Invalid token\" }");
                response.send(httpExchange);
                return;
            }

            // Get Leaderboard
            else if(httpExchange.getRequestMethod().equals(Method.GET.name())) {
                response = this.leaderboardController.getLeaderboard();
            }

            response.send(httpExchange);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
