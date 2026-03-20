package com.rubenac.saveslot.usergame;

import com.rubenac.saveslot.game.Game;
import com.rubenac.saveslot.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "user_games")
@Getter
@Setter
@NoArgsConstructor
public class UserGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(precision = 3, scale = 2)
    @DecimalMin("0.00")
    @DecimalMax("5.00")
    private BigDecimal rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "completion_percentage")
    @Min(0)
    @Max(100)
    private Integer completionPercentage;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_id")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "game_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_game_id")
    )
    private Game game;

}
