package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a media entry like a movie, book, or game.
 * Each entry has a title, description, type, genres, release year, age restriction,
 * average score, favorite status, a creator user, and a list of ratings.
 */
public class MediaEntry {

    private int mediaentryid;
    private int creatorId; // to assign each entry to a user (just creator can delete/edit)
    private String title;
    private String description;
    private String mediaType;
    private List<String> genres = new ArrayList<>();
    private int releaseYear;
    private int ageRestriction;
    private double avgscore;

    /**
     * Creates a new media entry with the given details.
     *
     * @param title the title of the media
     * @param description a short description
     * @param mediaType type of media (movie, book, game)
     * @param genres list of genres this media falls into
     * @param releaseYear year of release
     * @param agerestriction minimum age to view
     * @param creatorId the user who created this entry
     */
    public MediaEntry(String title, String description, String mediaType, int releaseYear, List<String> genres, int agerestriction, int creatorId) {
        this.title = title;
        this.description = description;
        this.mediaType = mediaType;
        this.releaseYear = releaseYear;
        this.ageRestriction = agerestriction;
        this.creatorId = creatorId;
        this.genres = genres;
    }

    public MediaEntry() {}

    /**
     * Getters and Setters
     */

    public int getMediaentryid() {
        return mediaentryid;
    }

    public void setMediaentryid(int mediaentryid) {
        this.mediaentryid = mediaentryid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediatype) {
        this.mediaType = mediatype;
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

    public int getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int agerestriction) {
        this.ageRestriction = agerestriction;
    }

    public double getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(double avgscore) {
        this.avgscore = avgscore;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
}
