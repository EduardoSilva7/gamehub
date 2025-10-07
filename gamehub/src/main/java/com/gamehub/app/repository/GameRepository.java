package com.gamehub.app.repository;

import com.gamehub.app.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> { }
