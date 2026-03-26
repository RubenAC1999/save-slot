package com.rubenac.saveslot.usergame;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findByUserId(UUID userId);
}
