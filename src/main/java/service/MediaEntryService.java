package service;

import model.MediaEntry;
import model.User;
import persistence.IMediaEntryRepository;

import java.util.List;

public class MediaEntryService implements IMediaEntryService {
    private static MediaEntryService instance;
    private final IMediaEntryRepository mediaEntryRepository;

    private MediaEntryService(IMediaEntryRepository mediaEntryRepository) {
        this.mediaEntryRepository = mediaEntryRepository;
    }

    public static MediaEntryService getInstance(IMediaEntryRepository mediaEntryRepository) {
        if (instance == null) {
            instance = new MediaEntryService(mediaEntryRepository);
        }
        return instance;
    }

    @Override
    public void addMediaEntry(MediaEntry mediaEntry) {
        mediaEntryRepository.addMediaEntry(mediaEntry);
    }

    @Override
    public void editMediaEntry(String mediaEntryId, String title, String description, String mediatype,
                               List<String> genres, int releaseYear, int agerestriction, User creator) {

        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry != null) {
            mediaEntryRepository.updateMediaEntry(mediaEntryId, title, description, mediatype,
                    genres, releaseYear, agerestriction, creator);
        }
    }

    @Override
    public void deleteMediaEntry(String title, String mediaType) {
        mediaEntryRepository.deleteMediaEntry(title, mediaType);
    }

    @Override
    public void favoriteMediaEntry(String mediaEntryId) {
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry != null) {
            mediaEntryRepository.setFavoriteStatus(mediaEntryId, true);
        }
    }

    @Override
    public void unFavoriteMediaEntry(String mediaEntryId) {
        MediaEntry mediaEntry = mediaEntryRepository.getMediaEntryByID(mediaEntryId);
        if (mediaEntry != null) {
            mediaEntryRepository.setFavoriteStatus(mediaEntryId, false);
        }
    }
}