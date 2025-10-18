package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.MediaEntry;
import service.IMediaEntryService;
import service.MediaEntryService;

import java.io.IOException;

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

    /**
     * Creates a new MediaEntryHandler with a given media entry service.
     *
     * @param mediaEntryService service used to manage media entries
     */
    public MediaEntryHandler(MediaEntryService mediaEntryService) {
        this.mediaEntryService = mediaEntryService;
    }

    /**
     * Adds a new media entry if all required fields are valid.
     * This logic will later be moved to the MediaEntryController,
     * and the handler will only forward the request.
     *
     * @param mediaEntry the media entry to add
     */
    public void addMediaEntry(MediaEntry mediaEntry) {
        if (mediaEntry.getTitle() != null
                && !mediaEntry.getGenres().isEmpty()
                && mediaEntry.getReleaseYear() != 0
                && mediaEntry.getAgerestriction() != 0
                && mediaEntry.getCreator() != null) {
            mediaEntryService.addMediaEntry(mediaEntry);
        }
    }

    /**
     * Deletes a media entry by title and media type.
     * This logic will also be moved to the MediaEntryController later,
     * while the handler will only handle the HTTP request forwarding.
     *
     * @param title the title of the media entry
     * @param mediaType the type of the media (Movie, book, game)
     */
    public void deleteMediaEntry(String title, String mediaType) {
        if (title != null) {
            mediaEntryService.deleteMediaEntry(title, mediaType);
        }
    }

    /**
     * Handles all incoming HTTP requests for media entries.
     * In the future, this method will:
     *
     * Check the HTTP request method (GET, POST, PUT, DELETE)
     * Parse request data and headers
     * Forward the request to the MediaEntryController
     * Return the controller’s response to the client
     *
     * @param exchange the HTTP exchange containing the request and response
     * @throws IOException if an input/output error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}