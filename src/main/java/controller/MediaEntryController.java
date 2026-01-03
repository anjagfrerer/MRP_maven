package controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import model.MediaEntry;
import model.Rating;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.server.Response;
import service.IMediaEntryService;
import service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MediaEntryController extends Controller{
    private static IMediaEntryService mediaEntryService;
    private static MediaEntryController instance;

    /**
     * creates an MediaEntryController with a corresponding IMediaEntryService, that is responsible for the MediaENtry CRUD logic.
     * @param mediaEntryService
     * @return
     */
    public MediaEntryController(IMediaEntryService mediaEntryService) {
        this.mediaEntryService = mediaEntryService;
    }

    /**
     * Returns a single instance of the MediaEntryController (Singleton pattern). If no instance exists, a new one is created.
     * @param mediaEntryService
     * @return
     */
    public static MediaEntryController getInstance(IMediaEntryService mediaEntryService) {
        if (instance == null) instance = new MediaEntryController(mediaEntryService);
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
    public Response createMediaEntry(String requestBody, User user)
    {
        try {
            MediaEntry requestMediaEntry = this.getObjectMapper().readValue(requestBody, MediaEntry.class);
            boolean success = mediaEntryService.addMediaEntry(new MediaEntry(requestMediaEntry.getTitle(), requestMediaEntry.getDescription(), requestMediaEntry.getMediaType(), requestMediaEntry.getReleaseYear(), requestMediaEntry.getGenres(), requestMediaEntry.getAgeRestriction(), user.getUserid()), user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry successfully added"))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response deleteMediaEntry(int mediaEntryId, User user)
    {
        try {
            boolean success = mediaEntryService.deleteMediaEntry(mediaEntryId, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry successfully deleted"))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response favoriteMediaEntry(int mediaEntryID, User user) {
        try {
            boolean success = mediaEntryService.favoriteMediaEntry(mediaEntryID, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry set as favorite"))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response unFavoriteMediaEntry(int mediaEntryID, User user) {
        try {
            boolean success = mediaEntryService.unFavoriteMediaEntry(mediaEntryID, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry delted from favorites"))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response updateMediaEntry(int mediaEntryID, String requestBody, User user) {
        try {
            MediaEntry mediaEntry = this.getObjectMapper().readValue(requestBody, MediaEntry.class);
            boolean success = mediaEntryService.editMediaEntry(mediaEntryID, mediaEntry, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry updated succesfully"))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response searchAndFilterMediaEntries(Map<String, String> queryParams) {
        try {
            String title = queryParams.get("title");
            String genre = queryParams.get("genre");
            String sortBy = queryParams.get("sortBy");

            List<MediaEntry> list = mediaEntryService.searchAndFilterMediaEntries(title, genre, sortBy);
            if(list!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(list)
                );
            }

            return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
                );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    public Response getMediaEntryById(int mediaEntryID, User user) {
        try {
            MediaEntry mediaEntry = mediaEntryService.getMediaEntryById(mediaEntryID, user);

            if(mediaEntry!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(mediaEntry)
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
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

    public Response getRecommendation(int userid, Map<String, String> queryParams, User user) {
        try {
            List<MediaEntry> mediaEntries = new ArrayList<MediaEntry>();
            String type = queryParams.get("type");
            if(type.equals("genre")) {
                mediaEntries = mediaEntryService.getRecommendationByGenre(userid, user);
            }else if(type.equals("content")) {
                mediaEntries = mediaEntryService.getRecommendationByContent(userid, user);
            }

            if(mediaEntries!=null) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(mediaEntries)
                );
            }

            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    getObjectMapper().writeValueAsString(Map.of("error", "An error occured"))
            );
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