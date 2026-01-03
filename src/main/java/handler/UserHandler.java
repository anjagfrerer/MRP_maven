package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.MediaEntryController;
import controller.RatingController;
import controller.UserController;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.http.Method;
import restserver.server.Request;
import restserver.server.Response;
import service.IUserService;
import service.MediaEntryService;
import service.RatingService;
import service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class handles all user-related HTTP requests.
 * It connects the HTTP server with the UserController.
 * It processes actions like user login and registration.
 */
public class UserHandler implements HttpHandler {

    private final UserService userService;
    private UserController userController;
    private RatingController ratingController;
    private MediaEntryController mediaEntryController;

    /**
     * Creates a new UserHandler with a given UserService.
     * @param userService the service used for user operations
     */
    public UserHandler(UserService userService, RatingService ratingService, MediaEntryService mediaEntryService) {
        this.userService = userService;
        this.userController = UserController.getInstance(userService);
        this.ratingController = RatingController.getInstance(ratingService);
        this.mediaEntryController = MediaEntryController.getInstance(mediaEntryService);
    }

    /**
     * Handles all incoming HTTP requests related to user actions.
     * This method is automatically called by the HTTP server when a client
     * sends a request to a user-related URL (for example: "/users/login" or "/users/register").
     * It reads the request data, checks what kind of request it is (method and path),
     * and then calls the right function in the UserController.
     * The controller checks the login data and creates a Response which is then sent back to the user.
     *
     * If something goes wrong while reading the request or sending the response,
     * the method throws a RuntimeException.
     * @param httpExchange the HTTP exchange object that contains information about the incoming request (like URL, headers, and body)
     *                     and allows sending a response back to the client
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        try{
            Request request = new Request(httpExchange.getRequestURI());
            Response response = null;
            String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            // Login
            if (httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(2).equalsIgnoreCase("login")) {

                response = userController.login(requestBody);
                response.send(httpExchange);
                return;
            }
            // Register
            else if (httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(2).equalsIgnoreCase("register")) {

                response = userController.register(requestBody);
                response.send(httpExchange);
                return;
            }

            String authHeader = httpExchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response = new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON,
                        "{ \"error\": \"Missing or invalid token\" }");
                response.send(httpExchange);
                return;
            }

            String token = authHeader.substring("Bearer ".length());
            User user = userService.getUserByToken(token);

            if (user == null) {
                response = new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON,
                        "{ \"error\": \"Invalid token\" }");
                response.send(httpExchange);
                return;
            }

            // Get Profile
            else if (httpExchange.getRequestMethod().equals(Method.GET.name()) &&
                    request.getPathParts().size() > 3 &&
                    request.getPathParts().get(3).equalsIgnoreCase("profile")){
                response = this.userController.getProfile(Integer.parseInt(request.getPathParts().get(2)), user);
            }

            // Get Recommendation
            else if (httpExchange.getRequestMethod().equals(Method.GET.name()) &&
                    request.getPathParts().size() > 3 &&
                    request.getPathParts().get(3).equalsIgnoreCase("recommendations")){
                Map<String, String> queryParams = request.getQueryParams(httpExchange.getRequestURI());
                response = this.mediaEntryController.getRecommendation(Integer.parseInt(request.getPathParts().get(2)), queryParams, user);
            }

            // Get Rating History
            else if (httpExchange.getRequestMethod().equals(Method.GET.name()) &&
                    request.getPathParts().size() > 3 &&
                    request.getPathParts().get(3).equalsIgnoreCase("ratings")){
                response = this.ratingController.getRatingHistory(Integer.parseInt(request.getPathParts().get(2)), user);
            }

            // Get Favorites
            else if (httpExchange.getRequestMethod().equals(Method.GET.name()) &&
                    request.getPathParts().size() > 3 &&
                    request.getPathParts().get(3).equalsIgnoreCase("favorites")){
                response = this.userController.getFavorites(Integer.parseInt(request.getPathParts().get(2)), user);
            }

            // Update Profile
            else if (httpExchange.getRequestMethod().equals(Method.PUT.name()) &&
                    request.getPathParts().size() > 3 &&
                    request.getPathParts().get(3).equalsIgnoreCase("profile")){
                response = this.userController.updateProfile(Integer.parseInt(request.getPathParts().get(2)), requestBody, user);
            }

            response.send(httpExchange);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
