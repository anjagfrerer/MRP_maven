package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.MediaEntryController;
import controller.RatingController;
import controller.UserController;
import model.MediaEntry;
import model.User;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.http.Method;
import restserver.server.Request;
import restserver.server.Response;
import service.IMediaEntryService;
import service.IRatingService;
import service.MediaEntryService;
import service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * MediaEntryHandler is responsible for handling all HTTP requests related to media entries.
 * Currently, it provides simple methods for adding and deleting media entries.
 * In the future, all incoming HTTP requests (GET, POST, PUT, DELETE)
 * will be processed inside the handle() method.
 * These requests will then be forwarded to a new MediaEntryController
 * which will handle the actual business logic.
 */
public class MediaEntryHandler implements HttpHandler {

    private final IMediaEntryService mediaEntryService;
    private MediaEntryController mediaEntryController;
    private RatingController ratingController;

    /**
     * Creates a new MediaEntryHandler with a given media entry service.
     *
     * @param mediaEntryService service used to manage media entries
     */
    public MediaEntryHandler(IMediaEntryService mediaEntryService, IRatingService ratingService) {
        this.mediaEntryService = mediaEntryService;
        this.mediaEntryController = MediaEntryController.getInstance(mediaEntryService);
        this.ratingController = RatingController.getInstance(ratingService);
    }

    /**
     * Handles all incoming HTTP requests for media entries.
     * This method:
     *
     * Checks the HTTP request method (GET, POST, PUT, DELETE)
     * Parses request data and headers
     * Forwards the request to the MediaEntryController
     * Returns the controller’s response to the client
     *
     * @param httpExchange the HTTP exchange containing the request and response
     * @throws IOException if an input/output error occurs
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        try{
            Request request = new Request(httpExchange.getRequestURI());
            Response response = null;
            String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String authHeader = httpExchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response = new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON,
                        "{ \"error\": \"Missing or invalid token\" }");
                response.send(httpExchange);
                return;
            }

            String token = authHeader.substring("Bearer ".length());
            User user = UserService.getInstance(null).getUserByToken(token); // Singleton aus UserService verwenden

            if (user == null) {
                response = new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON,
                        "{ \"error\": \"Invalid token\" }");
                response.send(httpExchange);
                return;
            }

            // Rate Media Entry
            if(httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(3).equalsIgnoreCase("rate")){
                response = this.ratingController.rateMediaEntry(Integer.parseInt(request.getPathParts().get(2)),requestBody, user);
            }
            // Mark MediaEntry as favourite
            else if(httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 2 &&
                    request.getPathParts().get(3).equalsIgnoreCase("favorite")){
                response = this.mediaEntryController.favoriteMediaEntry(Integer.parseInt(request.getPathParts().get(2)), user);
            }
            // Unmark MediaEntry as favourite
            else if(httpExchange.getRequestMethod().equals(Method.DELETE.name()) &&
                    request.getPathParts().size() > 3 &&
                    request.getPathParts().get(3).equalsIgnoreCase("favorite")){
                response = this.mediaEntryController.unFavoriteMediaEntry(Integer.parseInt(request.getPathParts().get(2)), user);
            }
            // Delete Media Entry
            else if (httpExchange.getRequestMethod().equals(Method.DELETE.name()) &&
                    request.getPathParts().size() > 2){
                response = this.mediaEntryController.deleteMediaEntry(Integer.parseInt(request.getPathParts().get(2)), user);
            }
            // Get Media Entry By Id
            else if (httpExchange.getRequestMethod().equals(Method.GET.name()) &&
                    request.getPathParts().size() > 2){
                response = this.mediaEntryController.getMediaEntryById(Integer.parseInt(request.getPathParts().get(2)), user);
            }
            // Create Media Entry
            else if(httpExchange.getRequestMethod().equals(Method.POST.name()) &&
                    request.getPathParts().size() > 1){
                response = this.mediaEntryController.createMediaEntry(requestBody, user);
            }
            // Update Media Entry
            else if(httpExchange.getRequestMethod().equals(Method.PUT.name()) &&
                    request.getPathParts().size() > 2){
                response = this.mediaEntryController.updateMediaEntry(Integer.parseInt(request.getPathParts().get(2)), requestBody, user);
            }
            // Search / Filter Media Entry
            else if (httpExchange.getRequestMethod().equals(Method.GET.name()) &&
                    request.getPathParts().size() == 2) {

                Map<String, String> queryParams = request.getQueryParams(httpExchange.getRequestURI());

                response = this.mediaEntryController
                        .searchAndFilterMediaEntries(queryParams);
            }


            response.send(httpExchange);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}