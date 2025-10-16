package persistence;

import model.MediaEntry;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MediaEntryRepository implements IMediaEntryRepository {
    private final List<MediaEntry> mediaEntries;
    private static final MediaEntryRepository instance = new MediaEntryRepository();

    private MediaEntryRepository() {
        this.mediaEntries = new ArrayList<>();
    }

    public static MediaEntryRepository getInstance() {
        return instance;
    }

    @Override
    public List<MediaEntry> getALlMediaEntries() {
        return mediaEntries;
    }

    @Override
    public void addMediaEntry(MediaEntry mediaEntry) {
        mediaEntry.setId(UUID.randomUUID().toString());
        mediaEntries.add(mediaEntry);
    }

    @Override
    public void deleteMediaEntry(String mediaTitle, String mediaType) {
        mediaEntries.removeIf(entry ->
                entry.getTitle().equals(mediaTitle) && entry.getMediatype().equals(mediaType));
    }

    @Override
    public MediaEntry getMediaEntryByID(String id) {
        return mediaEntries.stream()
                .filter(mediaEntry -> mediaEntry.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateMediaEntry(String id, String title, String description, String mediatype,
                                 List<String> genres, int releaseYear, int agerestriction, User creator) {
        MediaEntry mediaEntry = getMediaEntryByID(id);
        if (mediaEntry != null) {
            mediaEntry.setTitle(title);
            mediaEntry.setDescription(description);
            mediaEntry.setMediatype(mediatype);
            mediaEntry.setGenres(genres);
            mediaEntry.setReleaseYear(releaseYear);
            mediaEntry.setAgerestriction(agerestriction);
            mediaEntry.setCreator(creator);
        }
    }

    @Override
    public void setFavoriteStatus(String id, boolean favorite) {
        MediaEntry mediaEntry = getMediaEntryByID(id);
        if (mediaEntry != null) {
            mediaEntry.setFavorite(favorite);
        }
    }
}