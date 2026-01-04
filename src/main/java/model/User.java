package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the application.
 * Stores user ID, username, password, total ratings, and average score.
 */
public class User {
    private int userid;
    private String username;
    private String password;
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
