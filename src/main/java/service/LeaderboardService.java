package service;

import model.MediaEntry;
import model.User;
import model.Profile;
import persistence.IMediaEntryRepository;
import persistence.IRatingRepository;
import persistence.IUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing media entries.
 * Provides methods to add, edit, delete, and manage favorite status of media entries.
 */
public class LeaderboardService implements ILeaderboardService {

    private static LeaderboardService instance;
    private IUserRepository userRepository;

    private LeaderboardService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static LeaderboardService getInstance(IUserRepository userRepository) {
        if (instance == null) {
            instance = new LeaderboardService(userRepository);
        }
        return instance;
    }

    @Override
    public List<Profile> getLeaderboard() {
        return userRepository.getLeaderboard();
    }
}