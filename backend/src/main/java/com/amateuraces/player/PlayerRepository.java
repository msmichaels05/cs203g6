package com.amateuraces.player;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 * For the purpose of this exercise, CrudRepository would also be sufficient
 */
@Repository
public interface PlayerRepository extends JpaRepository <Player, Long>{
    Player findByUserId(Long userId);
    Optional<Player> findOptionalByUserId(Long userId);
    Optional<Player> findByIdAndUserId(Long id, Long userId);
    Optional<Player> findByName(String name);
    Optional<Player> findByPhoneNumber(String phoneNumber);
}