package com.amateuraces.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amateuraces.project.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
}