package persistence;

import model.User;

import java.util.List;

public interface IUserRepository {
    public List<User> getAllUsers();
    public void createUser(String username, String password);
    public User getUserByUsername(String username);
}