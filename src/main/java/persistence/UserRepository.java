package persistence;

import database.DatabaseManager;
import model.MediaEntry;
import model.Profile;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repository class that manages all registered users and provides methods
 * to create and retrieve users.
 */
public class UserRepository implements IUserRepository {
    private static UserRepository instance = new UserRepository();

    /** Private constructor to prevent creating multiple instances. */
    private UserRepository() {
    }

    /**
     * Returns the single instance of this repository.
     *
     * @return the shared UserRepository instance
     */
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * Returns all registered users.
     *
     * @return list of all users
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT userid, username, password FROM mrp_user";
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("userid"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Creates a new user and adds it to the registered users list.
     *
     * @param user the user object containing username and password
     * @return true if user was created successfully, false otherwise
     */
    @Override
    public boolean createUser(User user) {
        String userSql =
                "INSERT INTO mrp_user (username, password) " +
                        "VALUES (?, ?)";

        String profileSql =
                "INSERT INTO profile (email, userid) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
            psUser.setString(1, user.getUsername());
            psUser.setString(2, user.getPassword());
            int affectedRows = psUser.executeUpdate();
            if (affectedRows == 0) return false;
            try (ResultSet rs = psUser.getGeneratedKeys()) {
                if (!rs.next()) return false;
                user.setUserid(rs.getInt(1));
            }
            try (PreparedStatement profilePs = conn.prepareStatement(profileSql)) {
                profilePs.setString(1, user.getUsername() + "@gmail.com");
                profilePs.setInt(2, user.getUserid());
                profilePs.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the found user or null if not found
     */
    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT userid, username, password FROM mrp_user WHERE username = ?";
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserid(rs.getInt("userid"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Retrieves the profile information of a user.
     *
     * @param userId the ID of the user
     * @return the Profile object or null if not found
     */
    @Override
    public Profile getProfile(int userId) {
        String sql = "SELECT p.profileid, u.username, p.email, p.favoritegenre, COUNT(r.ratingid) AS totalratings, AVG(r.stars) AS avg_score FROM mrp_user u LEFT JOIN profile p ON u.userid = p.userid LEFT JOIN rating r ON u.userid = r.creator WHERE u.userid = ? GROUP BY p.profileid, u.userid, p.email, p.favoritegenre, p.totalratings, p.avgscore";
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Profile profile = new Profile();
                    profile.setProfileId(rs.getInt("profileid"));
                    profile.setUsername(rs.getString("username"));
                    profile.setEmail(rs.getString("email"));
                    profile.setFavoriteGenre(rs.getString("favoritegenre"));
                    profile.setTotalRatings(rs.getInt("totalratings"));
                    profile.setAvgScore(rs.getDouble("avg_score"));
                    return profile;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Returns the favorite media entries of a user.
     *
     * @param userId the ID of the user
     * @return list of favorite media entries or null if an error occurs
     */
    @Override
    public List<MediaEntry> getFavorites(int userId) {
        String sql = "SELECT m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, AVG(r.stars) AS avg_score, STRING_AGG(DISTINCT g.name, ',') AS genres FROM mediaentry m JOIN favorite f ON m.mediaentryid = f.mediaentryid LEFT JOIN mediaentry_genre mg ON mg.mediaentryid = m.mediaentryid LEFT JOIN genre g ON mg.genreid = g.genreid LEFT JOIN rating r ON m.mediaentryid = r.mediaentryid WHERE f.userid = ? GROUP BY m.mediaentryid";
        List<MediaEntry> favorites = new ArrayList<>();
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MediaEntry mediaEntry = new MediaEntry();
                    mediaEntry.setMediaentryid(rs.getInt("mediaentryid"));
                    mediaEntry.setTitle(rs.getString("title"));
                    mediaEntry.setDescription(rs.getString("description"));
                    mediaEntry.setMediaType(rs.getString("media_type"));
                    mediaEntry.setReleaseYear(rs.getInt("release_year"));
                    mediaEntry.setAgeRestriction(rs.getInt("age_restriction"));
                    mediaEntry.setAvgscore(rs.getInt("avg_score"));

                    // Genres als List setzen
                    String genresStr = rs.getString("genres");
                    List<String> genres = genresStr != null ? List.of(genresStr.split(",")) : new ArrayList<>();
                    mediaEntry.setGenres(genres);

                    favorites.add(mediaEntry);
                }
                return favorites;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the profile information of a user.
     *
     * @param userId the ID of the user
     * @param email new email
     * @param favoritegenre new favorite genre
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateProfile(int userId, String email, String favoritegenre) {
        String sql = "UPDATE profile SET email = COALESCE(?, email), favoritegenre = COALESCE(?, favoritegenre) WHERE userid = ?";
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, favoritegenre);
            ps.setInt(3, userId);
            return ps.executeUpdate() == 1;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the leaderboard of users based on total ratings.
     *
     * @return list of Profile objects sorted by total ratings in descending order
     */
    @Override
    public List<Profile> getLeaderboard() {
        List<Profile> leaderboard = new ArrayList<>();
        List<User> users = getAllUsers();
        for (User user : users) {
            Profile profile = getProfile(user.getUserid());
            if (profile != null) {
                leaderboard.add(profile);
            }
        }
        leaderboard.sort((p1, p2) -> Integer.compare(p2.getTotalRatings(), p1.getTotalRatings()));
        return leaderboard;
    }

    /**
     * Resets the singleton instance (for testing purposes).
     */
    public static void resetInstance() {
        instance = null;
    }
}
