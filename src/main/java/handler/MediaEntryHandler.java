package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.MediaEntry;
import service.IMediaEntryService;
import service.MediaEntryService;

import java.io.IOException;

public class MediaEntryHandler implements HttpHandler {

    private final IMediaEntryService mediaEntryService;


    public MediaEntryHandler(MediaEntryService mediaEntryService) {
        this.mediaEntryService = mediaEntryService;
    }

    public void addMediaEntry(MediaEntry mediaEntry) {
        if(mediaEntry.getTitle()!=null && !mediaEntry.getGenres().isEmpty() && mediaEntry.getReleaseYear()!=0 && mediaEntry.getAgerestriction()!=0 && mediaEntry.getCreator()!=null) {
            mediaEntryService.addMediaEntry(mediaEntry);
        }
    }

    public void deleteMediaEntry(String title, String mediaType) {
        // Hier checken, wegen Berechtigung
        if(title!=null) {
            mediaEntryService.deleteMediaEntry(title, mediaType);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
