package service;

import model.MediaEntry;
import model.User;
import persistence.IMediaEntryRepository;

import java.util.List;


/**
 * Service class for managing media entries.
 * Provides methods to add, edit, delete, and manage favorite status of media entries.
 */
public class MediaEntryService implements IMediaEntryService {
    private static MediaEntryService instance;
    private final IMediaEntryRepository mediaEntryRepository;

    /**
     * Private constructor for singleton pattern.
     *
     * @param mediaEntryRepository the repository used to store media entries
     */
    private MediaEntryService(IMediaEntryRepository mediaEntryRepository) {
        this.mediaEntryRepository = mediaEntryRepository;
    }

    /**
     * Returns the single instance of MediaEntryService.
     *
     * @param mediaEntryRepository the repository used to store media entries
     * @return the instance
     */
    public static MediaEntryService getInstance(IMediaEntryRepository mediaEntryRepository) {
        if (instance == null) {
            instance = new MediaEntryService(mediaEntryRepository);
        }
        return instance;
    }

    /**
     * Adds a new media entry.
     *
     * @param mediaEntry the media entry to add
     */
    @Override
    public void addMediaEntry(MediaEntry mediaEntry) {
        mediaEntryRepository.addMediaEntry(mediaEntry);
    }

    /**
     * Edits an existing media entry identified by its ID.
     *
     * @param mediaEntryId the ID of the media entry to edit
     * @param title new title
     * @param description new description
     * @param mediatype new type
     * @param genres new list of genres
     * @param releaseYear new release year
     * @param agerestriction new age restriction
     * @param creator the user who owns the media entry
     */
    @Override
    public void editMediaEntry(String mediaEntryId, String title, String description, String mediatype,
                               List<String> genres, int releaseYear, int agerestriction, User creator) {

        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry != null) {
            mediaEntryRepository.updateMediaEntry(mediaEntryId, title, description, mediatype,
                    genres, releaseYear, agerestriction, creator);
        }
    }

    /**
     * Deletes a media entry by title and type.
     *
     * @param title the title of the media entry
     * @param mediaType the type of the media entry (e.g., movie, series)
     */
    @Override
    public void deleteMediaEntry(String title, String mediaType) {
        mediaEntryRepository.deleteMediaEntry(title, mediaType);
    }

    /**
     * Marks a media entry as favorite.
     *
     * @param mediaEntryId the ID of the media entry
     */
    @Override
    public void favoriteMediaEntry(String mediaEntryId) {
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry != null) {
            mediaEntryRepository.setFavoriteStatus(mediaEntryId, true);
        }
    }

    /**
     * Removes a media entry from favorites.
     *
     * @param mediaEntryId the ID of the media entry
     */
    @Override
    public void unFavoriteMediaEntry(String mediaEntryId) {
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry != null) {
            mediaEntryRepository.setFavoriteStatus(mediaEntryId, false);
        }
    }
}