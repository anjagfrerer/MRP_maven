package persistence;

import model.User;

import java.util.List;

/**
 * Interface for managing users in the repository.
 * Provides methods to create and retrieve user data.
 */
public interface IUserRepository {
    public List<User> getAllUsers();
    public void createUser(String username, String password);
    public User getUserByUsername(String username);
}