package service;

import static org.junit.jupiter.api.Assertions.*;

import controller.LeaderboardController;
import model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import restserver.http.ContentType;
import restserver.http.HttpStatus;
import restserver.server.Response;
import service.ILeaderboardService;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

class LeaderboardControllerTest {

    private ILeaderboardService leaderboardService;
    private LeaderboardController leaderboardController;

    @BeforeEach
    void setUp() {
        leaderboardService = mock(ILeaderboardService.class);
        leaderboardController = new LeaderboardController(leaderboardService);
    }

    @BeforeEach
    void resetSingleton() {
        LeaderboardService.resetInstance();
    }

    @Test
    void testGetLeaderboardReturnsProfiles() throws Exception {
        // Arrange: Beispiel-Daten
        Profile user1 = new Profile(1, "alice", 3, 3.7, "thriller", "alice@gmail.com");
        Profile user2 = new Profile(2, "bob", 7, 4.7, "fantasy", "bob@gmail.com");
        List<Profile> mockLeaderboard = Arrays.asList(user1, user2);

        when(leaderboardService.getLeaderboard()).thenReturn(mockLeaderboard);

        // Act
        Response response = leaderboardController.getLeaderboard();

        // Assert: HTTP Status und Content-Type
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());

        // Body enthält die Benutzernamen
        String body = response.getContent();
        assertTrue(body.contains("alice"));
        assertTrue(body.contains("bob"));

        // Service wurde genau einmal aufgerufen
        verify(leaderboardService, times(1)).getLeaderboard();
    }

    @Test
    void testGetLeaderboardReturnsEmptyList() throws Exception {
        when(leaderboardService.getLeaderboard()).thenReturn(null);

        Response response = leaderboardController.getLeaderboard();

        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertTrue(response.getContent().contains("[]"));

        verify(leaderboardService, times(1)).getLeaderboard();
    }
}
