package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Controller class that provides access to a Jackson ObjectMapper instance, which is used for converting
 * between Java objects and JSON strings. It provides shared functionality for all controllers in this project.
 */
public class Controller {
    private ObjectMapper objectMapper;

    public Controller() {
        this.objectMapper = new ObjectMapper();

        // JavaTimeModule registrieren, damit LocalDateTime unterstützt wird
        objectMapper.registerModule(new JavaTimeModule());

        // Verhindert, dass LocalDateTime als Timestamp geschrieben wird
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}