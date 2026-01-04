package service;

import model.MediaEntry;
import model.Profile;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.IUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private IUserRepository userRepository;
    private UserService userService;

    // Simulierte In-Memory-Datenbank
    private Map<String, User> mockDb;
    private Map<Integer, Profile> mockProfiles;

    @BeforeEach
    void setup() {
        // Singleton zurücksetzen
        UserService.resetInstance();

        // Mock Repository erstellen
        userRepository = mock(IUserRepository.class);

        // In-Memory-Strukturen
        mockDb = new HashMap<>();
        mockProfiles = new HashMap<>();

        // getAllUsers()
        when(userRepository.getAllUsers()).thenAnswer(invocation -> new ArrayList<>(mockDb.values()));

        // createUser()
        when(userRepository.createUser(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            int id = mockDb.size() + 1;
            user.setUserid(id);
            mockDb.put(user.getUsername(), user);

            // Standardprofil erstellen
            Profile profile = new Profile();
            profile.setProfileId(id);
            profile.setUsername(user.getUsername());
            profile.setEmail(user.getUsername() + "@gmail.com");
            mockProfiles.put(id, profile);

            return true;
        });

        // getUserByUsername()
        when(userRepository.getUserByUsername(anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            return mockDb.get(username);
        });

        // getProfile(int userId)
        when(userRepository.getProfile(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return mockProfiles.get(userId);
        });

        // updateProfile(int userId, String email, String genre)
        when(userRepository.updateProfile(anyInt(), any(), any())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            String email = invocation.getArgument(1);
            String genre = invocation.getArgument(2);
            Profile profile = mockProfiles.get(userId);
            if (profile == null) return false;
            if (email != null) profile.setEmail(email);
            if (genre != null) profile.setFavoriteGenre(genre);
            return true;
        });

        // getFavorites(int userId)
        when(userRepository.getFavorites(anyInt())).thenAnswer(invocation -> new ArrayList<MediaEntry>());

        // UserService mit mock Repository
        userService = UserService.getInstance(userRepository);
    }

    @Test
    void testRegisterUser_SuccessAndDuplicate() {
        boolean created = userService.registerUser("testuser", "password123");
        assertTrue(created, "User sollte erfolgreich registriert werden");

        boolean duplicate = userService.registerUser("testuser", "password123");
        assertFalse(duplicate, "Doppelte Registrierung sollte fehlschlagen");
    }

    @Test
    void testLogin_SuccessAndFail() {
        userService.registerUser("loginuser", "secret");

        boolean success = userService.login("loginuser", "secret");
        assertTrue(success, "Login sollte erfolgreich sein");

        boolean fail = userService.login("loginuser", "wrongpassword");
        assertFalse(fail, "Login mit falschem Passwort sollte fehlschlagen");
    }

    @Test
    void testGenerateAndValidateToken() {
        userService.registerUser("tokenuser", "1234");
        userService.login("tokenuser", "1234");

        User user = userService.getUserByUsername("tokenuser");
        String token = userService.generateToken(user);

        User userFromToken = userService.getUserByToken(token);
        assertNotNull(userFromToken, "Token sollte einen User zurückgeben");
        assertEquals("tokenuser", userFromToken.getUsername(), "Token sollte zum richtigen User gehören");
    }

    @Test
    void testProfileGetAndUpdate() {
        userService.registerUser("profileuser", "pw");
        User user = userService.getUserByUsername("profileuser");

        Profile profile = userService.getProfile(user.getUserid(), user);
        assertNotNull(profile, "Profil sollte existieren");
        assertEquals("profileuser@gmail.com", profile.getEmail(), "Standard-Email sollte gesetzt sein");

        boolean updated = userService.updateProfile(user.getUserid(), "newmail@gmail.com", "Comedy", user);
        assertTrue(updated, "Profilupdate sollte erfolgreich sein");

        Profile updatedProfile = userService.getProfile(user.getUserid(), user);
        assertEquals("newmail@gmail.com", updatedProfile.getEmail(), "Email sollte aktualisiert sein");
        assertEquals("Comedy", updatedProfile.getFavoriteGenre(), "Favorit Genre sollte aktualisiert sein");
    }

    @Test
    void testGetFavorites() {
        userService.registerUser("favuser", "pw");
        User user = userService.getUserByUsername("favuser");

        List<MediaEntry> favorites = userService.getFavorites(user.getUserid(), user);
        assertNotNull(favorites, "Liste der Favoriten sollte nicht null sein");
        assertEquals(0, favorites.size(), "Neue User sollten keine Favoriten haben");
    }
}
