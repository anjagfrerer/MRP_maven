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

    private Map<String, User> mockDb;
    private Map<Integer, Profile> mockProfiles;

    @BeforeEach
    void setup() {
        UserService.resetInstance();

        userRepository = mock(IUserRepository.class);
        mockDb = new HashMap<>();
        mockProfiles = new HashMap<>();

        // getAllUsers() to simulate returning all current users from the database without using a real database
        when(userRepository.getAllUsers()).thenAnswer(invocation -> new ArrayList<>(mockDb.values()));

        // createUser() to simulate creating a new user and profile in the database
        when(userRepository.createUser(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            int id = mockDb.size() + 1;
            user.setUserid(id);
            mockDb.put(user.getUsername(), user);

            Profile profile = new Profile();
            profile.setProfileId(id);
            profile.setUsername(user.getUsername());
            profile.setEmail(user.getUsername() + "@gmail.com");
            mockProfiles.put(id, profile);

            return true;
        });

        // getUserByUsername() to simulate finding a user by username in the database
        when(userRepository.getUserByUsername(anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            return mockDb.get(username);
        });

        // getProfile(int userId) to simulate getting a users profile by user ID from mockProfiles
        when(userRepository.getProfile(anyInt())).thenAnswer(invocation -> {
            int userId = invocation.getArgument(0);
            return mockProfiles.get(userId);
        });

        // updateProfile(int userId, String email, String genre)
        // to simulate updating a users profile in mockProfiles, changing email or favorite genre if the profile exists
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

        // getFavorites(int userId) to simulate returning an empty list of favorite media for a user
        when(userRepository.getFavorites(anyInt())).thenAnswer(invocation -> new ArrayList<MediaEntry>());

        // UserService with mock Repository
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
    void testRegisterWithNullOrEmpty() {
        assertFalse(userService.registerUser(null, "pw"), "Null-Username sollte fehlschlagen");
        assertFalse(userService.registerUser("", "pw"), "Leerer Username sollte fehlschlagen");
        assertFalse(userService.registerUser("user", null), "Null-Passwort sollte fehlschlagen");
        assertFalse(userService.registerUser("user", ""), "Leeres Passwort sollte fehlschlagen");
    }

    @Test
    void testLoginWithoutRegister() {
        boolean success = userService.login("nonexistent", "pw");
        assertFalse(success, "Login eines nicht-registrierten Users sollte fehlschlagen");
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
    void testLoginWithNullOrEmptyPassword() {
        userService.registerUser("loginuser", "secret");
        assertFalse(userService.login("loginuser", null), "Null-Passwort sollte fehlschlagen");
        assertFalse(userService.login("loginuser", ""), "Leeres Passwort sollte fehlschlagen");
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
    void testTokenForNonexistentUser() {
        User fakeUser = new User("fakeuser", "pw");
        String token = userService.generateToken(fakeUser);
        User result = userService.getUserByToken(token);
        assertNull(result, "Token eines nicht registrierten Users sollte null zurückgeben");
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
    void testProfileUpdateByDifferentUser() {
        userService.registerUser("user1", "pw");
        userService.registerUser("user2", "pw");
        User user1 = userService.getUserByUsername("user1");
        User user2 = userService.getUserByUsername("user2");

        boolean updated = userService.updateProfile(user1.getUserid(), "blabla@gmail.com", "Horror", user2);
        assertFalse(updated, "Fremder User sollte Profil nicht ändern können");
    }

    @Test
    void testProfileWithNullUser() {
        userService.registerUser("randomuser", "pw");
        Profile profile = userService.getProfile(1, null);
        assertNull(profile, "Profilabfrage ohne User sollte null zurückgeben");
    }

    @Test
    void testProfileUpdateWithNullValues() {
        userService.registerUser("user3", "pw");
        User user = userService.getUserByUsername("user3");

        boolean updated = userService.updateProfile(user.getUserid(), null, null, user);
        assertTrue(updated, "Update mit null Werten sollte true zurückgeben");
        Profile profile = userService.getProfile(user.getUserid(), user);
        assertEquals("user3@gmail.com", profile.getEmail(), "Email sollte unverändert bleiben");
        assertNull(profile.getFavoriteGenre(), "Favorite Genre sollte null bleiben");
    }

    @Test
    void testGetFavorites() {
        userService.registerUser("favuser", "pw");
        User user = userService.getUserByUsername("favuser");

        List<MediaEntry> favorites = userService.getFavorites(user.getUserid(), user);
        assertNotNull(favorites, "Liste der Favoriten sollte nicht null sein");
        assertEquals(0, favorites.size(), "Neue User sollten keine Favoriten haben");
    }

    @Test
    void testGetFavoritesWithNullUser() {
        List<MediaEntry> favorites = userService.getFavorites(1, null);
        assertNull(favorites, "Favorites-Abfrage ohne User sollte null zurückgeben");
    }

    @Test
    void testDuplicateTokenHandling() {
        userService.registerUser("duplicateuser", "pw1");
        User user1 = userService.getUserByUsername("duplicateuser");
        String token1 = userService.generateToken(user1);
        userService.login("duplicateuser", "pw1");

        userService.registerUser("duplicateuser2", "pw2");
        User user2 = userService.getUserByUsername("duplicateuser2");
        String token2 = userService.generateToken(user2);

        assertNotEquals(token1, token2, "Tokens verschiedener User sollten unterschiedlich sein");
    }

}
