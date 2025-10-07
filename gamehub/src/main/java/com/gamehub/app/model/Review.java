package com.gamehub.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "REVIEWS")
public class Review {

    @Id
    @Column(name = "REVIEW_ID", nullable = false)
    private Long reviewId;

    @NotNull
    @Column(name = "GAME_ID", nullable = false)
    private Long gameId;

    @Digits(integer = 1, fraction = 1)
    @Column(name = "RATING", precision = 2, scale = 1)
    private BigDecimal rating;

    @Size(max = 255)
    @Column(name = "REVIEW_TEXT", length = 255)
    private String reviewText;

    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}
