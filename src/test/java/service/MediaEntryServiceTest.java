package service;

import model.MediaEntry;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.IMediaEntryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediaEntryServiceTest {

    private IMediaEntryRepository repository;
    private MediaEntryService service;
    private User user;
    private MediaEntry mediaEntry;

    @BeforeEach
    void setUp() {
        repository = mock(IMediaEntryRepository.class);
        service = MediaEntryService.getInstance(repository);

        user = new User();
        user.setUserid(1);

        mediaEntry = new MediaEntry();
        mediaEntry.setMediaentryid(1);
        mediaEntry.setTitle("Test Movie");
        mediaEntry.setDescription("A great movie");
        mediaEntry.setMediaType("movie");
        mediaEntry.setGenres(List.of("Action"));
        mediaEntry.setReleaseYear(2023);
        mediaEntry.setAgeRestriction(12);
        mediaEntry.setCreatorId(user.getUserid());
    }

    @BeforeEach
    void resetSingleton() {
        MediaEntryService.resetInstance();
    }

    @Test
    void testAddMediaEntry() {
        when(repository.addMediaEntry(mediaEntry)).thenReturn(true);

        boolean result = service.addMediaEntry(mediaEntry, user);

        assertTrue(result, "MediaEntry should be added successfully");
        verify(repository).addMediaEntry(mediaEntry);
    }

    @Test
    void testEditMediaEntryByCreator() {
        MediaEntry updated = new MediaEntry();
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Description");
        updated.setMediaType("movie");
        updated.setGenres(List.of("Action"));
        updated.setReleaseYear(2024);
        updated.setAgeRestriction(12);

        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        when(repository.updateMediaEntry(
                eq(1),
                anyString(),
                anyString(),
                anyString(),
                anyList(),
                anyInt(),
                anyInt(),
                anyInt()
        )).thenReturn(true);

        boolean result = service.editMediaEntry(1, updated, user);

        assertTrue(result, "Creator should be able to edit MediaEntry");
    }

    @Test
    void testEditMediaEntryByNonCreator() {
        MediaEntry updated = new MediaEntry();
        updated.setTitle("Updated Title");
        User otherUser = new User();
        otherUser.setUserid(2);

        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);

        boolean result = service.editMediaEntry(1, updated, otherUser);

        assertFalse(result, "Non-creator should not be able to edit MediaEntry");
    }

    @Test
    void testDeleteMediaEntry() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        when(repository.deleteMediaEntry(1)).thenReturn(true);

        boolean result = service.deleteMediaEntry(1, user);

        assertTrue(result, "Creator should be able to delete MediaEntry");
        verify(repository).deleteMediaEntry(1);
    }

    @Test
    void testFavoriteAndUnFavoriteMediaEntry() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        when(repository.setFavoriteStatus(user.getUserid(), 1)).thenReturn(true);
        when(repository.setUnFavoriteStatus(user.getUserid(), 1)).thenReturn(true);

        boolean favResult = service.favoriteMediaEntry(1, user);
        boolean unfavResult = service.unFavoriteMediaEntry(1, user);

        assertTrue(favResult, "MediaEntry should be favorited successfully");
        assertTrue(unfavResult, "MediaEntry should be unfavorited successfully");
    }

    @Test
    void testSearchAndFilterMediaEntries() {
        List<MediaEntry> list = new ArrayList<>();
        list.add(mediaEntry);

        when(repository.searchAndFilterMediaEntries("Test", "Action", "title")).thenReturn(list);

        List<MediaEntry> result = service.searchAndFilterMediaEntries("Test", "Action", "title");

        assertEquals(1, result.size(), "Search should return 1 media entry");
        assertEquals("Test Movie", result.get(0).getTitle());
    }

    @Test
    void testGetRecommendationByGenreAndContent() {
        List<MediaEntry> recommendations = new ArrayList<>();
        recommendations.add(mediaEntry);

        when(repository.getRecommendationByGenre(user.getUserid())).thenReturn(recommendations);
        when(repository.getRecommendationByContent(user.getUserid())).thenReturn(recommendations);

        List<MediaEntry> genreRec = service.getRecommendationByGenre(user.getUserid(), user);
        List<MediaEntry> contentRec = service.getRecommendationByContent(user.getUserid(), user);

        assertEquals(1, genreRec.size(), "Genre recommendation should return 1 entry");
        assertEquals(1, contentRec.size(), "Content recommendation should return 1 entry");
    }
}
