package service;

import model.Profile;
import model.User;

import java.util.List;

/**
 * Service interface for managing the leaderboard.
 * Provides methods to retrieve user rankings.
 */
public interface ILeaderboardService {

    List<Profile> getLeaderboard();
}