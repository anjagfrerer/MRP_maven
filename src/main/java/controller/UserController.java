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

    // hat wieder instance methode
    // bekommt als argument einen UserService
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    public static UserController getInstance(IUserService userService) {
        if (instance == null) instance = new UserController(userService);
        return instance;
    }

    // GET /users/:username :password
    public Response login(String requestBody)
    {
        try {
            // "{\"Username\":\"max\",\"Password\":\"1234\"}";
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
                        HttpStatus.UNAUTHORIZED,
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

    public Response register(String requestBody)
    {
        try {
            // "{\"Username\":\"max\",\"Password\":\"1234\"}";
            User requestUser = this.getObjectMapper().readValue(requestBody, User.class);
            boolean success = userService.registerUser(requestUser.getUsername(), requestUser.getPassword());

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "Registration successful"))
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