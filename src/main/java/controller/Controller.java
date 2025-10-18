package controller;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller class that provides access to a Jackson ObjectMapper instance, which is used for converting
 * between Java objects and JSON strings. It provides shared functionality for all controllers in this project.
 */
public class Controller {
    private ObjectMapper objectMapper;

    public Controller() {
        this.objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}