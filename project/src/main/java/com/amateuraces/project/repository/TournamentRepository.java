package com.amateuraces.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amateuraces.project.entity.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}