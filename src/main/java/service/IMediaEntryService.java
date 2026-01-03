package service;

import model.MediaEntry;
import model.User;

import java.util.List;

/**
 * Service interface for managing media entries.
 * Provides methods for adding, editing, deleting, and favoriting media entries.
 */
public interface IMediaEntryService {

    boolean addMediaEntry(MediaEntry mediaEntry, User user);

    boolean deleteMediaEntry(int id, User user);

    boolean editMediaEntry(int mediaEntryId, MediaEntry updatedEntry, User user);

    boolean favoriteMediaEntry(int mediaEntryId, User user);

    boolean unFavoriteMediaEntry(int mediaEntryId, User user);

    List<MediaEntry> getAllMediaEntries();

    List<MediaEntry> searchAndFilterMediaEntries(String title, String genre, String sortBy);

    List<MediaEntry> fullSearchAndFilterMediaEntries(String title, String genre, String mediaType,
                                                     int releaseYear, int ageRestriction, int minRating,
                                                     String sortBy);

    MediaEntry getMediaEntryById(int mediaEntryID, User user);

    List<MediaEntry> getRecommendationByGenre(int userid, User user);

    List<MediaEntry> getRecommendationByContent(int userid, User user);
}