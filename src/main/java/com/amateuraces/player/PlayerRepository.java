package com.amateuraces.player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    Long save(Player player);
    int update(Player player);
    int deleteById(Long id);
    List<Player> findAll();

    // Using Optional - the return value of this method may contain a null value
    Optional<Player> findById(Long id);
 
}
