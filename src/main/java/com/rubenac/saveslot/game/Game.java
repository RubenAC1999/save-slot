package com.rubenac.saveslot.game;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "games")
@NoArgsConstructor
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rawg_id", nullable = false, unique = true)
    private Integer rawgId;

    @Column(nullable = false)
    private String title;

    @Column(name = "cover_image_url", nullable = false, unique = true)
    private String coverImageUrl;

    private String synopsis;

    private String company;

    @Column(name = "release_date")
    private LocalDate releaseDate;
}
