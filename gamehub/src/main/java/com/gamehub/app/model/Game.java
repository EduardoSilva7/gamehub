package com.gamehub.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "GAMES")
public class Game {

    @Id
    @Column(name = "GAME_ID", nullable = false)
    private Long gameId;

    @NotBlank
    @Size(max = 120)
    @Column(name = "TITLE", length = 120, nullable = false)
    private String title;

    @Size(max = 60)
    @Column(name = "GENRE", length = 60)
    private String genre;

    @Column(name = "RELEASE_YEAR", precision = 4, scale = 0)
    private Integer releaseYear;

    @Digits(integer = 8, fraction = 2)
    @Column(name = "PRICE", precision = 10, scale = 2)
    private BigDecimal price;

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
