package persistence;

import model.MediaEntry;

import java.util.List;
import java.util.Map;

/**
 * Interface for managing media entries in the repository.
 * Provides methods to add, delete, update, retrieve, search, and filter media entries.
 */
public interface IMediaEntryRepository {

    List<MediaEntry> getAllMediaEntries();
    boolean addMediaEntry(MediaEntry mediaEntry);
    boolean deleteMediaEntry(int id);
    MediaEntry getMediaEntryByID(int id);
    boolean updateMediaEntry(int id, String title, String description, String mediatype, List<String> genres, int releaseYear, int agerestriction, int creatorId);
    boolean setFavoriteStatus(int userid, int mediaentryid);
    boolean setUnFavoriteStatus(int userid, int mediaentryid);
    List<MediaEntry> searchAndFilterMediaEntries(String title, String genre, String sortBy);
    List<MediaEntry> fullSearchAndFilterMediaEntries(Map<String, Object> filters, String sortBy);
    List<MediaEntry> getRecommendationByGenre(int userid);
    List<MediaEntry> getRecommendationByContent(int userid);

}