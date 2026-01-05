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
    void testAddMediaEntryNullEntry() {
        boolean result = service.addMediaEntry(null, user);
        assertFalse(result, "Adding null media entry should fail");
    }

    @Test
    void testAddMediaEntryNullUser() {
        boolean result = service.addMediaEntry(mediaEntry, null);
        assertFalse(result, "Adding media entry with null user should fail");
    }

    @Test
    void testAddMediaEntryRepositoryFailure() {
        when(repository.addMediaEntry(mediaEntry)).thenReturn(false);
        boolean result = service.addMediaEntry(mediaEntry, user);
        assertFalse(result, "Repository failure should result in false");
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
    void testEditMediaEntryNonExistentId() {
        when(repository.getMediaEntryByID(99)).thenReturn(null);
        boolean result = service.editMediaEntry(99, mediaEntry, user);
        assertFalse(result, "Editing non-existent media entry should fail");
    }

    @Test
    void testEditMediaEntryNullUser() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        boolean result = service.editMediaEntry(1, mediaEntry, null);
        assertFalse(result, "Editing with null user should fail");
    }

    @Test
    void testEditMediaEntryRepositoryFailure() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        when(repository.updateMediaEntry(
                anyInt(), anyString(), anyString(), anyString(), anyList(), anyInt(), anyInt(), anyInt()))
                .thenReturn(false);

        boolean result = service.editMediaEntry(1, mediaEntry, user);
        assertFalse(result, "Repository update failure should return false");
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
    void testDeleteMediaEntryNonExistent() {
        when(repository.getMediaEntryByID(99)).thenReturn(null);
        boolean result = service.deleteMediaEntry(99, user);
        assertFalse(result, "Deleting non-existent media entry should fail");
    }

    @Test
    void testDeleteMediaEntryNonCreator() {
        User otherUser = new User();
        otherUser.setUserid(2);
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);

        boolean result = service.deleteMediaEntry(1, otherUser);
        assertFalse(result, "Non-creator should not be able to delete");
    }

    @Test
    void testDeleteMediaEntryRepositoryFailure() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        when(repository.deleteMediaEntry(1)).thenReturn(false);

        boolean result = service.deleteMediaEntry(1, user);
        assertFalse(result, "Repository failure should result in false");
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
    void testFavoriteNonExistentMedia() {
        when(repository.getMediaEntryByID(99)).thenReturn(null);
        boolean result = service.favoriteMediaEntry(99, user);
        assertFalse(result, "Favoriting non-existent media should fail");
    }

    @Test
    void testFavoriteNullUser() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        boolean result = service.favoriteMediaEntry(1, null);
        assertFalse(result, "Favoriting with null user should fail");
    }

    @Test
    void testFavoriteRepositoryFailure() {
        when(repository.getMediaEntryByID(1)).thenReturn(mediaEntry);
        when(repository.setFavoriteStatus(user.getUserid(), 1)).thenReturn(false);

        boolean result = service.favoriteMediaEntry(1, user);
        assertFalse(result, "Repository failure should result in false");
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
    void testSearchNoResults() {
        when(repository.searchAndFilterMediaEntries("Unknown", "Action", "title")).thenReturn(new ArrayList<>());
        List<MediaEntry> result = service.searchAndFilterMediaEntries("Unknown", "Action", "title");
        assertTrue(result.isEmpty(), "Search with no results should return empty list");
    }

    @Test
    void testSearchNullParameters() {
        when(repository.searchAndFilterMediaEntries(null, null, null)).thenReturn(List.of(mediaEntry));
        List<MediaEntry> result = service.searchAndFilterMediaEntries(null, null, null);
        assertEquals(1, result.size(), "Null search params should not fail");
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

    @Test
    void testRecommendationEmpty() {
        when(repository.getRecommendationByGenre(user.getUserid())).thenReturn(new ArrayList<>());
        when(repository.getRecommendationByContent(user.getUserid())).thenReturn(new ArrayList<>());

        List<MediaEntry> genreRec = service.getRecommendationByGenre(user.getUserid(), user);
        List<MediaEntry> contentRec = service.getRecommendationByContent(user.getUserid(), user);

        assertTrue(genreRec.isEmpty(), "Empty genre recommendations should return empty list");
        assertTrue(contentRec.isEmpty(), "Empty content recommendations should return empty list");
    }

    @Test
    void testRecommendationNullUser() {
        List<MediaEntry> genreRec = service.getRecommendationByGenre(user.getUserid(), null);
        List<MediaEntry> contentRec = service.getRecommendationByContent(user.getUserid(), null);

        assertTrue(genreRec == null || genreRec.isEmpty(), "Null user should result in empty genre recommendations");
        assertTrue(contentRec == null || contentRec.isEmpty(), "Null user should result in empty content recommendations");

    }
}