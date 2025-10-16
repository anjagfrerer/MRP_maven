package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Rating {

    private MediaEntry mediaEntry;
    private int stars;
    private String comment;
    private String id;
    private User creator;
    private LocalDateTime localDate;
    private List<User> likes = new ArrayList<>();

    public Rating(String id, MediaEntry mediaEntry, int stars, String comment, User creator) {
        this.id = id;
        this.mediaEntry = mediaEntry;
        this.stars = stars;
        this.comment = comment;
        this.creator = creator;
        this.localDate = null;
    }

    public MediaEntry getMediaEntry() {
        return mediaEntry;
    }

    public void setMediaEntry(MediaEntry mediaEntry) {
        this.mediaEntry = mediaEntry;
    }

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDateTime localDate) {
        this.localDate = localDate;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
