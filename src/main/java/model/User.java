package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userid;
    private String username;
    private String password;
    private int interactions; //to view leaderbord of most active users
    private List<MediaEntry> favorites;
    private List<Rating> ratings; //!

    public User(String id, String username, String password) {
        this.username = username;
        this.password = password;
        this.interactions = 0;
        this.favorites = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.userid = id;
    }

    public User() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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
}
