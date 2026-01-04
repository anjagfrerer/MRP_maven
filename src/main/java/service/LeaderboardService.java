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
 * Service class for managing the leaderboard.
 * Provides methods to retrieve user rankings based on profile statistics.
 */
public class LeaderboardService implements ILeaderboardService {

    private static LeaderboardService instance;
    private IUserRepository userRepository;

    /**
     * Private constructor to prevent multiple instances.
     *
     * @param userRepository the user repository used to get leaderboard data
     */
    private LeaderboardService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the single instance of the leaderboard service.
     *
     * @param userRepository the user repository
     * @return the shared LeaderboardService instance
     */
    public static LeaderboardService getInstance(IUserRepository userRepository) {
        if (instance == null) {
            instance = new LeaderboardService(userRepository);
        }
        return instance;
    }

    /**
     * Returns the leaderboard of users.
     *
     * @return list of Profile objects sorted by ranking
     */
    @Override
    public List<Profile> getLeaderboard() {
        return userRepository.getLeaderboard();
    }

    /**
     * Resets the singleton instance (mainly for testing purposes).
     */
    public static void resetInstance() {
        instance = null;
    }
}