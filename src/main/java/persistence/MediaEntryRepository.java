package persistence;

import model.MediaEntry;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class that manages all media entries.
 */
public class MediaEntryRepository implements IMediaEntryRepository {
    private final List<MediaEntry> mediaEntries;
    private static final MediaEntryRepository instance = new MediaEntryRepository();

    /** Private constructor to prevent creating multiple instances. */
    private MediaEntryRepository() {
        this.mediaEntries = new ArrayList<>();
    }

    /**
     * Returns the single instance of the repository.
     *
     * @return the shared MediaEntryRepository instance
     */
    public static MediaEntryRepository getInstance() {
        return instance;
    }

    /**
     * Returns all media entries.
     *
     * @return list of all media entries
     */
    @Override
    public List<MediaEntry> getALlMediaEntries() {
        return mediaEntries;
    }

    /**
     * Adds a new media entry and assigns it a random unique ID.
     *
     * @param mediaEntry the media entry to add
     */
    @Override
    public void addMediaEntry(MediaEntry mediaEntry) {
        mediaEntry.setId(UUID.randomUUID().toString());
        mediaEntries.add(mediaEntry);
    }

    /**
     * Deletes a media entry by title and type.
     *
     * @param mediaTitle title of the media
     * @param mediaType type of the media (Movie, book, game)
     */
    @Override
    public void deleteMediaEntry(String mediaTitle, String mediaType) {
        mediaEntries.removeIf(entry ->
                entry.getTitle().equals(mediaTitle) && entry.getMediatype().equals(mediaType));
    }

    /**
     * Finds a media entry by its ID.
     *
     * @param id the ID of the media entry
     * @return the found media entry
     */
    @Override
    public MediaEntry getMediaEntryByID(String id) {
        return mediaEntries.stream()
                .filter(mediaEntry -> mediaEntry.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing media entry with new data.
     *
     * @param id the ID of the media entry
     * @param title new title
     * @param description new description
     * @param mediatype new type
     * @param genres new genres
     * @param releaseYear new release year
     * @param agerestriction new age restriction
     * @param creator user who wrote the entry
     */
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

    /**
     * Changes the favorite status of a media entry.
     *
     * @param id the ID of the media entry
     * @param favorite true to mark as favorite, false otherwise
     */
    @Override
    public void setFavoriteStatus(String id, boolean favorite) {
        MediaEntry mediaEntry = getMediaEntryByID(id);
        if (mediaEntry != null) {
            mediaEntry.setFavorite(favorite);
        }
    }
}