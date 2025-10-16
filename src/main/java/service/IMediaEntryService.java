package service;

import model.MediaEntry;
import model.User;

import java.util.List;

public interface IMediaEntryService {
    void addMediaEntry(MediaEntry mediaEntry);
    void deleteMediaEntry(String title, String mediaType);
    void editMediaEntry(String mediaEntryId, String title, String description, String mediatype, List<String> genres, int releaseYear, int agerestriction, User creator);
    void favoriteMediaEntry(String mediaEntryId);
    void unFavoriteMediaEntry(String mediaEntryId);
}
