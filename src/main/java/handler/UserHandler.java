package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.UserController;
import model.User;
import restserver.http.Method;
import restserver.server.Request;
import restserver.server.Response;
import service.IUserService;
import service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class handles all user-related HTTP requests.
 * It connects the HTTP server with the UserController.
 * It processes actions like user login and registration.
 */
public class UserHandler implements HttpHandler {

    private final IUserService userService;
    private UserController userController;

    /**
     * Creates a new UserHandler with a given UserService.
     * @param userService the service used for user operations
     */
    public UserHandler(UserService userService) {
        this.userService = userService;
        this.userController = UserController.getInstance(userService);
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
            if(httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(2).equalsIgnoreCase("login")){
                response = this.userController.login(requestBody);
            }
            // Register
            else if (httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(2).equalsIgnoreCase("register")){
                response = this.userController.register(requestBody);
            }

            response.send(httpExchange);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * All following methods are not yet implemented
     */

    public void editProfile(User user, String username, String password) {

    }

    public void createMediaEntry() {
    }

    public void updateMediaEntry() {
    }

    public void deleteMediaEntry() {
    }

    public void favoriteMediaEntry() {
    }

    public void unFavoriteMediaEntry() {
    }

    public void editRating() {
    }

    public void deleteRating() {
    }

    public void likeRating() {
    }

    public void viewFavorites() {
    }

    public void viewRatings() {
    }

    public void getRecommendations() {
    }
}
