package dto;

import java.time.LocalDateTime;

public class RatingHistoryDTO {
    private int stars;
    private String comment;
    private int likes;
    private LocalDateTime createdAt;
    private int mediaEntryId;
    private String mediaTitle;
    private String mediaType;

    public RatingHistoryDTO() {

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getMediaEntryId() {
        return mediaEntryId;
    }

    public void setMediaEntryId(int mediaEntryId) {
        this.mediaEntryId = mediaEntryId;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}