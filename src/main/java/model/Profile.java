package model;

/**
 * Represents a user profile.
 * Stores username, email, total ratings, average score, favorite genre and profile ID.
 */
public class Profile {
    private int profileId;
    private String username;
    private int totalRatings;
    private double avgScore;
    private String favoriteGenre;
    private String email;

    /**
     * Creates a new Profile with the given details.
     *
     * @param profileId unique ID of the profile
     * @param username username of the user
     * @param totalRatings total number of ratings by the user
     * @param avgScore average score given by the user
     * @param favoriteGenre user's favorite genre
     * @param email email of the user
     */
    public Profile(int profileId, String username, int totalRatings, double avgScore, String favoriteGenre, String email) {
        this.profileId = profileId;
        this.username = username;
        this.totalRatings = totalRatings;
        this.avgScore = avgScore;
        this.favoriteGenre = favoriteGenre;
        this.email = email;
    }

    public Profile() {}

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }
}
