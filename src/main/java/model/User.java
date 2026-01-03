package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the application.
 * A user has an ID, username, password, a list of favorite media,
 * ratings they created, and an interaction counter for ranking activity.
 */
public class User {
    private int userid;
    private String username;
    private String password;
    private int interactions; //to view leaderbord of most active users
    private List<MediaEntry> favorites;
    private List<Rating> ratings; //!
    private int totalRatings;
    private double avgScore;

    /**
     * Creates a new user with the given data.
     *
     * @param username username
     * @param password password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.interactions = 0;
        this.favorites = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.userid = 0;
    }

    public User() {
    }

    /**
     * Getters and Setters
     */

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInteractions() {
        return interactions;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }

    public List<MediaEntry> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<MediaEntry> favorites) {
        this.favorites = favorites;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }
}
