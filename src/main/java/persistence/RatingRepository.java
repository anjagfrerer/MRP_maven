package persistence;

import database.DatabaseManager;
import model.MediaEntry;
import model.Rating;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Repository class that manages all ratings and provides methods
 * to create, edit, delete, and like ratings.
 */
public class RatingRepository implements IRatingRepository {
    private static RatingRepository instance = new RatingRepository();

    /** Private constructor to prevent creating multiple instances. */
    private RatingRepository() {
    }

    /**
     * Returns the single instance of this repository.
     *
     * @return the shared RatingRepository instance
     */
    public static RatingRepository getInstance() {
        return instance;
    }

    /**
     * Adds a like from a user to a rating.
     *
     * @param user the user who liked the rating
     * @param rating the rating being liked
     */
    @Override
    public boolean likeRating(int ratingid, User user) {
        String sql = "INSERT INTO likes (userid, ratingid) VALUES (?, ?)";
        try (Connection connection = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserid());
            ps.setInt(2, ratingid);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new rating and adds it to the list.
     * Generates a random unique ID and sets the current date.
     *
     * @param user the creator of the rating
     * @param stars number of stars
     * @param comment optional comment
     * @param mediaEntry the media being rated
     */
    @Override
    public boolean rateMediaEntry(int mediaentryid, int stars, String comment, User user) {
        String sql = "INSERT INTO rating (mediaentryid, stars, comment, creator) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mediaentryid);
            ps.setInt(2, stars);
            ps.setString(3, comment);
            ps.setInt(4, user.getUserid());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) {
                return false; // MediaEntry existiert nicht
            }
            return false;
        }
    }

    @Override
    public boolean updateRating(int mediaentryid, int stars, String comment, User user) {
        String sql = "UPDATE rating SET stars = ?, comment = ? WHERE mediaentryid = ?";
        try (Connection connection = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stars);
            ps.setString(2, comment);
            ps.setInt(3, mediaentryid);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Rating> getRatingHistory(int userId, User user) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT r.*, COUNT(l.ratingid) AS likes FROM rating r LEFT JOIN likes l ON r.ratingid = l.ratingid WHERE r.creator = ? AND r.confirmed = true GROUP BY r.ratingid, mediaentryid, creator, stars, comment, created_at";
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("ratingid"));
                rating.setCreator(rs.getInt("creator"));
                rating.setStars(rs.getInt("stars"));
                rating.setComment(rs.getString("comment"));
                rating.setLocalDate(rs.getTimestamp("created_at").toLocalDateTime());
                rating.setLikes(rs.getInt("likes"));
                ratings.add(rating);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    @Override
    public Rating getRatingById(int ratingid) {
        String sql = "SELECT r.ratingid, r.creator, r.stars, r.comment, r.created_at, COUNT(l.userid) AS likes FROM rating r LEFT JOIN likes l ON r.ratingid = l.ratingid WHERE r.ratingid = ? GROUP BY r.ratingid, r.creator, r.stars, r.comment, r.created_at";
        try(Connection conn = DatabaseManager.INSTANCE.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ratingid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("ratingid"));
                rating.setCreator(rs.getInt("creator"));
                rating.setStars(rs.getInt("stars"));
                rating.setComment(rs.getString("comment"));
                rating.setLocalDate(rs.getTimestamp("created_at").toLocalDateTime());
                rating.setLikes(rs.getInt("likes"));
                return rating;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean confirmRatingComment(int ratingid) {
        String sql = "UPDATE rating SET confirmed = true WHERE ratingid = ?";
        try (Connection connection = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ratingid);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) {
                return false; // MediaEntry existiert nicht
            }
            return false;
        }
    }
}