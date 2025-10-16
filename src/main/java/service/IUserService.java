package service;

import model.User;

public interface IUserService {
    boolean login(String username, String password);
    boolean registerUser(String username, String password);
    User getUserByUsername(String username);
    String generateToken(User requestUser);
    User getUserByToken(String token);
}
