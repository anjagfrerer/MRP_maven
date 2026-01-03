package service;

import model.MediaEntry;
import model.Profile;
import model.User;
import persistence.IUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing users and authentication.
 * Provides methods to register, log in, check passwords, and manage authentication tokens.
 */
public class UserService implements IUserService {
    private static UserService instance;
    private final List<User> loggedInUsers;
    private IUserRepository userRepository;
    private Map<String, User> activeTokens = new HashMap<>();

    /**
     * Private constructor for singleton pattern.
     *
     * @param userRepository the repository used to store users
     */
    private UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.loggedInUsers = new ArrayList<>();
    }

    /**
     * Returns the single instance of UserService.
     *
     * @param repository the repository used to store users
     * @return the instance
     */
    public static UserService getInstance(IUserRepository repository) {
        if (instance == null) instance = new UserService(repository);
        return instance;
    }

    /**
     * Logs in a user with username and password.
     * If successful, generates an authentication token and adds the user to the logged-in list.
     *
     * @param username the users username
     * @param password the users password
     * @return true if login is successful, false otherwise
     */
    @Override
    public boolean login(String username, String password) {
        if (checkPassword(username, password)) {
            User found = userRepository.getUserByUsername(username);
            // check whether a token is valid, you need a mapping from Token --> User
            String token = generateToken(found);
            activeTokens.put(token, found);
            loggedInUsers.add(found);
            return true;
        }

        return false;
    }

    /**
     * Registers a new user with the given username and password.
     * Fails if the username already exists.
     *
     * @param username the desired username
     * @param password the desired password
     * @return true if registration is successful, false if username already exists
     */
    @Override
    public boolean registerUser(String username, String password) {
        List<User> allUsers = userRepository.getAllUsers();
        for (User u : allUsers) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }
        User newUser = new User(username, password);
        boolean created = userRepository.createUser(newUser);
        if (created) {
            String token = generateToken(newUser);
            activeTokens.put(token, newUser);
        }
        return created;
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the user if found, null otherwise
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    /**
     * Checks if the given password matches the users password.
     *
     * @param username the user's username
     * @param password the password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String username, String password) {
        User found = userRepository.getUserByUsername(username);
        if (found != null) {
            return found.getPassword().equals(password);
        } else {
            return false;
        }
    }

    /**
     * Generates a token for a user.
     *
     * @param user the user to generate a token for
     * @return a token string
     */
    @Override
    public String generateToken(User user) {
        return user.getUsername() + "-mrpToken";
    }

    /**
     * Finds a user by their token.
     *
     * @param token the token string
     * @return the associated user, or null if token is invalid
     */
    @Override
    public User getUserByToken(String token) {
        return activeTokens.get(token);
    }

    @Override
    public Profile getProfile(int userId, User user) {
        if (user == null) return null;
        return userRepository.getProfile(userId);
    }

    @Override
    public List<MediaEntry> getFavorites(int userId, User user) {
        if (user == null) return null;
        return userRepository.getFavorites(userId);
    }

    @Override
    public boolean updateProfile(int userId, String email, String favoritegenre, User user) {
        if (user == null || user.getUserid() != userId) return false;
        return userRepository.updateProfile(userId, email, favoritegenre);
    }
}
