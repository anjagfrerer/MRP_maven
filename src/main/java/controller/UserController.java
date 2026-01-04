package controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import model.MediaEntry;
import model.Profile;
import model.Rating;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.server.Response;
import service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserController extends Controller{
    private static IUserService userService;
    private static UserController instance;

    /**
     * Creates a new UserController.
     *
     * @param userService service used for user logic
     * @return no return value (constructor)
     */
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Returns the single instance of UserController.
     * Creates a new one if it does not exist.
     *
     * @param userService service used for user logic
     * @return UserController instance
     */
    public static UserController getInstance(IUserService userService) {
        if (instance == null) instance = new UserController(userService);
        return instance;
    }

    /**
     * Logs a user in.
     *
     * @param requestBody JSON data with username and password
     * @return HTTP response with token or error message
     */
    public Response login(String requestBody)
    {
        try {
            User requestUser = this.getObjectMapper().readValue(requestBody, User.class);
            boolean success = userService.login(requestUser.getUsername(), requestUser.getPassword());

            if(success) {
                User user = userService.getUserByUsername(requestUser.getUsername());
                String token = userService.generateToken(user);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Login successful.", "token", token))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Invalid credentials."))
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
     * Registers a new user.
     *
     * @param requestBody JSON data with username and password
     * @return HTTP response with token or error message
     */
    public Response register(String requestBody)
    {
        try {
            User requestUser = this.getObjectMapper().readValue(requestBody, User.class);
            boolean success = userService.registerUser(requestUser.getUsername(), requestUser.getPassword());

            if(success) {
                String token = userService.generateToken(requestUser);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Registration successful.", "token", token))
                );
            }else{
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Username already exists."))
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
     * Gets the profile of a user.
     *
     * @param userId ID of the user
     * @param user the requesting user
     * @return HTTP response with the profile
     */
    public Response getProfile(int userId, User user) {
        try {
            Profile profile = userService.getProfile(userId, user);

            if(profile!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(profile)
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Could not load profile."))
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
     * Gets the favorite media entries of a user.
     *
     * @param userId ID of the user
     * @param user the requesting user
     * @return HTTP response with a list of favorites
     */
    public Response getFavorites(int userId, User user) {
        try {
            List<MediaEntry> favorites = userService.getFavorites(userId, user);

            if(favorites!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(favorites)
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Could not load favorites."))
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
     * Updates the profile of a user.
     *
     * @param userId ID of the user
     * @param requestBody JSON data with profile information
     * @param user the requesting user
     * @return HTTP response with success or error message
     */
    public Response updateProfile(int userId, String requestBody, User user) {
        try {
            Profile profile = this.getObjectMapper().readValue(requestBody, Profile.class);
            boolean success = userService.updateProfile(userId, profile.getEmail(), profile.getFavoriteGenre(), user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Profile Update successful."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "Profile Update was not successful."))
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