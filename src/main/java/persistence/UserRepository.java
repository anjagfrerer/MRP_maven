package persistence;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository implements IUserRepository {
    private List<User> registeredUsers;
    //private List<User> loggedInUsers;
    private static final UserRepository instance = new UserRepository();

    private UserRepository() {
        this.registeredUsers = new ArrayList<>();
    }

    public static UserRepository getInstance() {
        return instance;
    }

    @Override
    public List<User> getAllUsers() {
        return registeredUsers;
    }

    @Override
    public void createUser(String username, String password) {
        registeredUsers.add(new User(UUID.randomUUID().toString(), username, password));
    }

    @Override
    public User getUserByUsername(String username) {
        return registeredUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
