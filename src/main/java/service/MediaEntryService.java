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
     */
    @Override
    public boolean addMediaEntry(MediaEntry mediaEntry, User user) {
        if (user == null || mediaEntry == null) return false;

        // Creator ist immer der eingeloggte User
        mediaEntry.setCreatorId(user.getUserid());

        return mediaEntryRepository.addMediaEntry(mediaEntry);
    }

    /**
     * Edits an existing media entry identified by its ID.
     */
    @Override
    public boolean editMediaEntry(int mediaEntryId, MediaEntry updatedEntry, User user) {
        if (user == null || updatedEntry == null) return false;

        MediaEntry existing = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (existing == null) return false; // 404

        // nur Creator darf ändern
        if (existing.getCreatorId() != user.getUserid()) return false; // 403

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
     * Deletes a media entry by id.
     */
    @Override
    public boolean deleteMediaEntry(int mediaEntryId, User user) {
        if (user == null) return false;

        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry == null) return false;

        // Nur Creator darf löschen
        if (mediaEntry.getCreatorId() != user.getUserid()) return false;

        return mediaEntryRepository.deleteMediaEntry(mediaEntryId);
    }

    /**
     * Marks a media entry as favorite.
     */
    @Override
    public boolean favoriteMediaEntry(int mediaEntryId, User user) {
        if (user == null) return false;

        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry == null) return false;

        return mediaEntryRepository.setFavoriteStatus(user.getUserid(), mediaEntryId);
    }

    /**
     * Removes a media entry from favorites.
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
     */
    @Override
    public List<MediaEntry> getAllMediaEntries() {
        return mediaEntryRepository.getALlMediaEntries();
    }

    @Override
    public List<MediaEntry> searchAndFilterMediaEntries(String title, String genre, String sortBy) {
        return mediaEntryRepository.searchAndFilterMediaEntries(title, genre, sortBy);
    }

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

    @Override
    public MediaEntry getMediaEntryById(int mediaEntryID, User user) {
        return mediaEntryRepository.getMediaEntryByID(mediaEntryID);
    }

    @Override
    public List<MediaEntry> getRecommendationByGenre(int userid, User user) {
        if(user == null) return null;
        if(userid!=user.getUserid()) return null;
        return mediaEntryRepository.getRecommendationByGenre(userid);
    }

    @Override
    public List<MediaEntry> getRecommendationByContent(int userid, User user) {
        if(user == null) return null;
        if(userid!=user.getUserid()) return null;
        return mediaEntryRepository.getRecommendationByContent(userid);
    }
}