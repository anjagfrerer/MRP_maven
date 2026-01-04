package service;

import model.MediaEntry;
import model.User;
import persistence.IMediaEntryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return the singleton instance
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
     * @param user the user creating the media entry
     * @return true if added successfully, false otherwise
     */
    @Override
    public boolean addMediaEntry(MediaEntry mediaEntry, User user) {
        if (user == null || mediaEntry == null) return false;
        // Creator = logged-in User
        mediaEntry.setCreatorId(user.getUserid());
        return mediaEntryRepository.addMediaEntry(mediaEntry);
    }

    /**
     * Edits an existing media entry by ID.
     *
     * @param mediaEntryId the ID of the media entry to edit
     * @param updatedEntry the updated media entry data
     * @param user the user performing the edit
     * @return true if edit is successful, false otherwise
     */
    @Override
    public boolean editMediaEntry(int mediaEntryId, MediaEntry updatedEntry, User user) {
        if (user == null || updatedEntry == null) return false;
        MediaEntry existing = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (existing == null) return false;
        // only Creator can edit
        if (existing.getCreatorId() != user.getUserid()) return false;
        return mediaEntryRepository.updateMediaEntry(
                mediaEntryId,
                updatedEntry.getTitle(),
                updatedEntry.getDescription(),
                updatedEntry.getMediaType(),
                updatedEntry.getGenres(),
                updatedEntry.getReleaseYear(),
                updatedEntry.getAgeRestriction(),
                user.getUserid()
        );
    }

    /**
     * Deletes a media entry by ID.
     *
     * @param mediaEntryId the ID of the media entry to delete
     * @param user the user performing the deletion
     * @return true if deletion is successful, false otherwise
     */
    @Override
    public boolean deleteMediaEntry(int mediaEntryId, User user) {
        if (user == null) return false;
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry == null) return false;
        // only Creator can delete
        if (mediaEntry.getCreatorId() != user.getUserid()) return false;
        return mediaEntryRepository.deleteMediaEntry(mediaEntryId);
    }

    /**
     * Marks a media entry as favorite for a user.
     *
     * @param mediaEntryId the ID of the media entry
     * @param user the user marking as favorite
     * @return true if successful, false otherwise
     */
    @Override
    public boolean favoriteMediaEntry(int mediaEntryId, User user) {
        if (user == null) return false;
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry == null) return false;
        return mediaEntryRepository.setFavoriteStatus(user.getUserid(), mediaEntryId);
    }

    /**
     * Removes a media entry from user's favorites.
     *
     * @param mediaEntryId the ID of the media entry
     * @param user the user performing the action
     * @return true if successful, false otherwise
     */
    @Override
    public boolean unFavoriteMediaEntry(int mediaEntryId, User user) {
        if (user == null) return false;
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry == null) return false;
        return mediaEntryRepository.setUnFavoriteStatus(user.getUserid(), mediaEntryId);
    }

    /**
     * Returns all media entries.
     *
     * @return list of all media entries
     */
    @Override
    public List<MediaEntry> getAllMediaEntries() {
        return mediaEntryRepository.getAllMediaEntries();
    }

    /**
     * Searches and filters media entries by title, genre, and sort order.
     *
     * @param title filter by title
     * @param genre filter by genre
     * @param sortBy sort order (e.g., "title", "year", "score")
     * @return filtered list of media entries
     */
    @Override
    public List<MediaEntry> searchAndFilterMediaEntries(String title, String genre, String sortBy) {
        return mediaEntryRepository.searchAndFilterMediaEntries(title, genre, sortBy);
    }

    /**
     * Searches and filters media entries with multiple filters.
     *
     * @param title filter by title
     * @param genre filter by genre
     * @param mediaType filter by media type
     * @param releaseYear filter by release year
     * @param ageRestriction filter by age restriction
     * @param minRating minimum rating to include
     * @param sortBy sort order
     * @return filtered list of media entries
     */
    @Override
    public List<MediaEntry> fullSearchAndFilterMediaEntries(String title, String genre, String mediaType, int releaseYear, int ageRestriction, int minRating, String sortBy) {
        Map<String, Object> filters = new HashMap<>();
        if (title != null && !title.isBlank()) filters.put("title", title);
        if (genre != null && !genre.isBlank()) filters.put("genre", genre);
        if (mediaType != null && !mediaType.isBlank()) filters.put("mediaType", mediaType);
        if (releaseYear >= 0) filters.put("releaseYear", releaseYear);
        if (ageRestriction >= 0) filters.put("ageRestriction", ageRestriction);

        List<MediaEntry> entries = mediaEntryRepository.fullSearchAndFilterMediaEntries(filters, sortBy);

        if (minRating >= 0) {
            entries = entries.stream()
                    .filter(m -> m.getAvgscore() >= minRating)
                    .toList();
        }

        return entries;
    }

    /**
     * Returns a media entry by ID.
     *
     * @param mediaEntryID the ID of the media entry
     * @param user the user performing the request
     * @return the MediaEntry object
     */
    @Override
    public MediaEntry getMediaEntryById(int mediaEntryID, User user) {
        return mediaEntryRepository.getMediaEntryByID(mediaEntryID);
    }

    /**
     * Returns recommended media entries by genre for a user.
     *
     * @param userid the ID of the user
     * @param user the user performing the request
     * @return list of recommended MediaEntry objects
     */
    @Override
    public List<MediaEntry> getRecommendationByGenre(int userid, User user) {
        if(user == null) return null;
        if(userid!=user.getUserid()) return null;
        return mediaEntryRepository.getRecommendationByGenre(userid);
    }

    /**
     * Returns recommended media entries by content similarity for a user.
     *
     * @param userid the ID of the user
     * @param user the user performing the request
     * @return list of recommended MediaEntry objects
     */
    @Override
    public List<MediaEntry> getRecommendationByContent(int userid, User user) {
        if(user == null) return null;
        if(userid!=user.getUserid()) return null;
        return mediaEntryRepository.getRecommendationByContent(userid);
    }

    /**
     * Resets the singleton instance (mainly for testing purposes).
     */
    public static void resetInstance() {
        instance = null;
    }
}