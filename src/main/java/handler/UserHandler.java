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

public class UserHandler implements HttpHandler {

    private final IUserService userService;
    private UserController userController;

    public UserHandler(UserService userService) {
        this.userService = userService;
        this.userController = UserController.getInstance(userService);
    }

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
