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
     * Creates a new MediaEntryController.
     *
     * @param mediaEntryService service used for media entry logic
     * @return no return value (constructor)
     */
    public MediaEntryController(IMediaEntryService mediaEntryService) {
        this.mediaEntryService = mediaEntryService;
    }

    /**
     * Returns the single instance of MediaEntryController.
     * Creates a new one if it does not exist.
     *
     * @param mediaEntryService service used for media entry logic
     * @return MediaEntryController instance
     */
    public static MediaEntryController getInstance(IMediaEntryService mediaEntryService) {
        if (instance == null) instance = new MediaEntryController(mediaEntryService);
        return instance;
    }

    /**
     * Creates a new media entry.
     *
     * @param requestBody JSON data of the media entry
     * @param user the user who creates the media entry
     * @return HTTP response with success or error message
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
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry successfully added."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "MediaEntry was not added."))
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
     * Deletes a media entry.
     *
     * @param mediaEntryId ID of the media entry
     * @param user the user who deletes the media entry
     * @return HTTP response with success or error message
     */
    public Response deleteMediaEntry(int mediaEntryId, User user)
    {
        try {
            boolean success = mediaEntryService.deleteMediaEntry(mediaEntryId, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry successfully deleted."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "MediaEntry was not deleted."))
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
     * Marks a media entry as favorite.
     *
     * @param mediaEntryID ID of the media entry
     * @param user the user who favorites the media entry
     * @return HTTP response with success or error message
     */
    public Response favoriteMediaEntry(int mediaEntryID, User user) {
        try {
            boolean success = mediaEntryService.favoriteMediaEntry(mediaEntryID, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry set as favorite."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "MediaEntry could not be set as favorite."))
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
     * Removes a media entry from favorites.
     *
     * @param mediaEntryID ID of the media entry
     * @param user the user who removes the favorite
     * @return HTTP response with success or error message
     */
    public Response unFavoriteMediaEntry(int mediaEntryID, User user) {
        try {
            boolean success = mediaEntryService.unFavoriteMediaEntry(mediaEntryID, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry deleted from favorites."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "MediaEntry was not deleted from favorites."))
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
     * Updates an existing media entry.
     *
     * @param mediaEntryID ID of the media entry
     * @param requestBody JSON data with updated values
     * @param user the user who updates the media entry
     * @return HTTP response with success or error message
     */
    public Response updateMediaEntry(int mediaEntryID, String requestBody, User user) {
        try {
            MediaEntry mediaEntry = this.getObjectMapper().readValue(requestBody, MediaEntry.class);
            boolean success = mediaEntryService.editMediaEntry(mediaEntryID, mediaEntry, user);

            if(success) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("message", "MediaEntry updated successfully."))
                );
            }else{
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        getObjectMapper().writeValueAsString(Map.of("error", "MediaEntry could not be updated."))
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
     * Searches and filters media entries.
     *
     * @param queryParams map with search and filter parameters
     * @return HTTP response with a list of media entries
     */
    public Response searchAndFilterMediaEntries(Map<String, String> queryParams) {
        try {
            String title = queryParams.get("title");
            String genre = queryParams.get("genre");
            String sortBy = queryParams.get("sortBy");
            String mediaType = queryParams.get("mediaType");
            Object releaseYear = queryParams.get("releaseYear");
            Object ageRestriction = queryParams.get("ageRestriction");
            Object rating = queryParams.get("rating");
            List<MediaEntry> list;
            if(mediaType != null || releaseYear != null || ageRestriction != null || rating != null) {
                int releaseYearInt = -1;
                int ageRestrictionInt = -1;
                int ratingInt = -1;
                try {
                    if(queryParams.get("releaseYear") != null) releaseYearInt = Integer.parseInt(queryParams.get("releaseYear"));
                    if(queryParams.get("ageRestriction") != null) ageRestrictionInt = Integer.parseInt(queryParams.get("ageRestriction"));
                    if(queryParams.get("rating") != null) ratingInt = Integer.parseInt(queryParams.get("rating"));
                } catch (NumberFormatException e) {
                    return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON,
                            "{ \"error\" : \"Invalid number format in filter parameters\" }");
                }
                try {
                    list = mediaEntryService.fullSearchAndFilterMediaEntries(title, genre, mediaType, releaseYearInt, ageRestrictionInt, ratingInt, sortBy);
                }catch(NumberFormatException e) {
                    e.printStackTrace();
                    list = null;
                }
            }else{
                list = mediaEntryService.searchAndFilterMediaEntries(title, genre, sortBy);
            }
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
                    getObjectMapper().writeValueAsString(Map.of("error", "An error occurred while filtering."))
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

    /**
     * Gets a media entry by its ID.
     *
     * @param mediaEntryID ID of the media entry
     * @param user the requesting user
     * @return HTTP response with the media entry
     */
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
                        getObjectMapper().writeValueAsString(Map.of("error", "Could not get MediaEntry by ID."))
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
     * Gets media recommendations for a user.
     *
     * @param userid ID of the user
     * @param queryParams parameters for recommendation type
     * @param user the requesting user
     * @return HTTP response with recommended media entries
     */
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
                    getObjectMapper().writeValueAsString(Map.of("error", "Could not load recommendations."))
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