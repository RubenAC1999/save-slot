package com.rubenac.saveslot.game;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    private String synopsis;

    @Column(nullable = false)
    private String company;
}
