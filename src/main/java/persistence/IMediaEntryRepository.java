package persistence;

import model.MediaEntry;
import model.User;

import java.util.List;

public interface IMediaEntryRepository {
    List<MediaEntry> getALlMediaEntries();

    void addMediaEntry(MediaEntry mediaEntry);

    void deleteMediaEntry(String mediaTitle, String mediaType);

    MediaEntry getMediaEntryByID(String id);

    void updateMediaEntry(String id, String title, String description, String mediatype,
                          List<String> genres, int releaseYear, int agerestriction, User creator);

    void setFavoriteStatus(String id, boolean favorite);
}