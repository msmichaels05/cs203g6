package com.amateuraces.tournament;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    // Custom query methods can be defined here if needed
Optional<Tournament> findByName(String name);

}
