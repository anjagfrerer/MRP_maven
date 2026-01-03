package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rating given by a user to a media entry.
 * Each rating has stars, a comment, a creator, a date, and a list of likes.
 */
public class Rating {

    private int ratingid;
    private int creatorid;
    private int stars;
    private String comment;
    private LocalDateTime localDate;
    private int likes;

    /**
     * Creates a new rating for a media entry.
     *
     * @param id unique id of the rating
     * @param mediaEntry the media being rated
     * @param stars number of stars
     * @param comment optional comment
     * @param creator the user who created the rating
     */
    public Rating(int id, int stars, String comment, int creatorid) {
        this.ratingid = id;
        this.stars = stars;
        this.comment = comment;
        this.creatorid = creatorid;
        this.localDate = null;
    }

    public Rating() {}

    /**
     * Getters and Setters
     */

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCreatorId() {
        return creatorid;
    }

    public void setCreator(int creatorid) {
        this.creatorid = creatorid;
    }

    public LocalDateTime getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDateTime localDate) {
        this.localDate = localDate;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return ratingid;
    }

    public void setId(int id) {
        this.ratingid = id;
    }
}
