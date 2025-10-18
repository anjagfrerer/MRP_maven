package service;

import model.User;

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
}
