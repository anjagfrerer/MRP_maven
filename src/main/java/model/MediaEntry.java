package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a media entry like a movie, book, or game.
 * Each entry has a title, description, type, genres, release year, age restriction,
 * average score, favorite status, a creator user, and a list of ratings.
 */
public class MediaEntry {

    private String title;
    private String description;
    private String mediatype;
    private String id;
    private int releaseYear;
    private int agerestriction;
    private int avgscore;
    private boolean favorite;
    private User creator; // to assign each entry to a user (just creator can delete/edit)
    private List<Rating> ratings = new ArrayList<>(); //!
    private List<String> genres = new ArrayList<>();

    /**
     * Creates a new media entry with the given details.
     *
     * @param title the title of the media
     * @param description a short description
     * @param mediatype type of media (movie, book, game)
     * @param genres list of genres this media falls into
     * @param releaseYear year of release
     * @param agerestriction minimum age to view
     * @param creator the user who created this entry
     */
    public MediaEntry(String title, String description, String mediatype, List<String> genres, int releaseYear, int agerestriction, User creator) {
        this.title = title;
        this.description = description;
        this.mediatype = mediatype;
        this.releaseYear = releaseYear;
        this.agerestriction = agerestriction;
        this.creator = creator;
        this.genres = genres;
        this.favorite = false;
    }

    /**
     * Getters and Setters
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getAgerestriction() {
        return agerestriction;
    }

    public void setAgerestriction(int agerestriction) {
        this.agerestriction = agerestriction;
    }

    public int getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(int avgscore) {
        this.avgscore = avgscore;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
