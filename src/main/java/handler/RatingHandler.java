package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.RatingController;
import model.MediaEntry;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.http.Method;
import restserver.server.Request;
import restserver.server.Response;
import service.IRatingService;
import service.RatingService;
import service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handles HTTP requests for ratings.
 * It forwards requests to the RatingController for business logic.
 */
public class RatingHandler implements HttpHandler {
    private final IRatingService ratingService;
    private RatingController ratingController;

    /**
     * Creates a new RatingHandler.
     *
     * @param ratingService service used to manage ratings
     */
    public RatingHandler(RatingService ratingService) {
        this.ratingService = ratingService;
        this.ratingController = RatingController.getInstance(ratingService);
    }

    /**
     * Handles all incoming HTTP requests for ratings.
     * Authenticates the user and forwards requests to the controller.
     *
     * @param httpExchange contains the HTTP request and response
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

            // Update Rating
            else if(httpExchange.getRequestMethod().equals(Method.PUT.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(1).equalsIgnoreCase("ratings")){
                response = this.ratingController.updateRating(Integer.parseInt(request.getPathParts().get(2)), requestBody, user);
            }

            // Like Rating
            else if(httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(3).equalsIgnoreCase("like")){
                response = this.ratingController.likeRating(Integer.parseInt(request.getPathParts().get(2)), user);
            }

            // Delete Rating
            else if(httpExchange.getRequestMethod().equals(Method.DELETE.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(3).equalsIgnoreCase("delete")){
                response = this.ratingController.deleteRating(Integer.parseInt(request.getPathParts().get(2)), user);
            }

            // Confirm Rating Comment
            else if(httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(3).equalsIgnoreCase("confirm")){
                response = this.ratingController.confirmRatingComment(Integer.parseInt(request.getPathParts().get(2)), user);
            }


            response.send(httpExchange);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
