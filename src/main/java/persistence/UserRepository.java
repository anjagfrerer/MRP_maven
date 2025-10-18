package persistence;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class that manages all registered users and provides methods
 * to create and retrieve users.
 */
public class UserRepository implements IUserRepository {
    private List<User> registeredUsers;
    //private List<User> loggedInUsers;
    private static final UserRepository instance = new UserRepository();

    /** Private constructor to prevent creating multiple instances. */
    private UserRepository() {
        this.registeredUsers = new ArrayList<>();
    }

    /**
     * Returns the single instance of this repository.
     *
     * @return the shared UserRepository instance
     */
    public static UserRepository getInstance() {
        return instance;
    }

    /**
     * Returns all registered users.
     *
     * @return list of all users
     */
    @Override
    public List<User> getAllUsers() {
        return registeredUsers;
    }

    /**
     * Creates a new user with a random unique ID
     * and adds it to the registered user list.
     *
     * @param username the user's username
     * @param password the user's password
     */
    @Override
    public void createUser(String username, String password) {
        registeredUsers.add(new User(UUID.randomUUID().toString(), username, password));
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the found user or null
     */
    @Override
    public User getUserByUsername(String username) {
        return registeredUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
