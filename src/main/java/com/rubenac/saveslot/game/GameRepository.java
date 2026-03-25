package com.rubenac.saveslot.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    Page<Game> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Game> findByCompanyContainingIgnoreCase(String company, Pageable pageable);
}
