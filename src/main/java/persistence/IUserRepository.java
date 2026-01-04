package persistence;

import model.MediaEntry;
import model.Profile;
import model.User;

import java.util.List;

/**
 * Interface for managing users in the repository.
 * Provides methods to create, retrieve, and update user data.
 */
public interface IUserRepository {
    public List<User> getAllUsers();
    public boolean createUser(User user);
    public User getUserByUsername(String username);
    Profile getProfile(int userId);
    List<MediaEntry> getFavorites(int userId);
    boolean updateProfile(int userId, String email, String favoritegenre);
    List<Profile> getLeaderboard();
}