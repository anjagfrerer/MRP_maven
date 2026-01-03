package service;

import model.Profile;
import model.User;

import java.util.List;

/**
 * Service interface for managing media entries.
 * Provides methods for adding, editing, deleting, and favoriting media entries.
 */
public interface ILeaderboardService {

    List<Profile> getLeaderboard();
}