package com.rubenac.saveslot.usergame;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findByStatus(Status status);
    List<UserGame> findByRating(BigDecimal rating);
    List<UserGame> findByUserUsername(String username);
    List<UserGame> findByGameTitle(String title);
}
