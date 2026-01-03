package service;

import model.MediaEntry;
import model.Profile;
import model.User;

import java.util.List;

/**
 * Service interface for managing users and authentication.
 * Provides methods to:
 * - log in a user with username and password
 * - register a new user
 * - find a user by username
 * - generate a token for a user
 * - retrieve a user by their token
 */
public interface IUserService {
    boolean login(String username, String password);
    boolean registerUser(String username, String password);
    User getUserByUsername(String username);
    String generateToken(User requestUser);
    User getUserByToken(String token);
    Profile getProfile(int userId, User user);

    List<MediaEntry> getFavorites(int userId, User user);

    boolean updateProfile(int userId, String email, String favoritegenre, User user);
}
