package controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.server.Response;
import service.IUserService;

import java.util.Map;

public class UserController extends Controller{
    private static IUserService userService;
    private static UserController instance;

    /**
     * creates an UserController with a corresponding IUserService, that is responsible for the registration and login logic.
     * @param userService
     * @return
     */
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Returns a single instance of the UserController (Singleton pattern). If no instance exists, a new one is created.
     * @param userService
     * @return
     */
    public static UserController getInstance(IUserService userService) {
        if (instance == null) instance = new UserController(userService);
        return instance;
    }

    /**
     * The login method reads the user's data from the request (in this case, username and password) and forwards it to the UserService,
     * which returns either a failed or successful login. A failed login can be caused by a non-existent username or an incorrect password.
     * If successful, a success message and the user's generated token are returned in a Response object. If a conflict occurs, an
     * error message is returned in a Response object.
     * @param requestBody data containing username und password
     * @return Response with the corresponding HTTPStatus, ContentType, and Content
     */
    public Response login(String requestBody)
    {
        try {
            User requestUser = this.getObjectMapper().readValue(requestBody, User.class);
            boolean success = userService.login(requestUser.getUsername(), requestUser.getPassword());

            if(success) {
                String token = userService.generateToken(requestUser);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Login successful", "token", token))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Invalid credentials"))
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
     * The login register reads the user's data from the request (in this case, username and password) and forwards it to the UserService,
     * which returns either a failed or successful registration. A failed registration can be caused by an already taken username.
     * If successful, a success message and the user's generated token are returned in a Response object. If a conflict occurs, an
     * error message is returned in a Response object.
     * @param requestBody data containing username und password
     * @return Response with the corresponding HTTPStatus, ContentType, and Content
     */
    public Response register(String requestBody)
    {
        try {
            // "{\"Username\":\"max\",\"Password\":\"1234\"}";
            User requestUser = this.getObjectMapper().readValue(requestBody, User.class);
            boolean success = userService.registerUser(requestUser.getUsername(), requestUser.getPassword());

            if(success) {
                String token = userService.generateToken(requestUser);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Registration successful", "token", token))
                );
            }else{
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Username already exists"))
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