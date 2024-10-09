package com.amateuraces.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amateuraces.book.Book;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> listPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayer(Long id){
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent())
            return player.get();
        else
            return null;
    }

    @Override
    public Player addPlayer(Player player) {  //Creating an account
        //return playerRepository.save(player);  // Save and return the new player
        player.setId(playerRepository.save(player));
        return player;
    }

    @Override
    public Player updatePlayer(Long id, Player updatedPlayerInfo) {
        Optional<Player> existingPlayer = playerRepository.findById(id);

        Player player = updatedPlayerInfo;
        player.setId(id);
        return playerRepository.update(player)>0 ? player : null;
    }

    @Override
    public int deletePlayer(Long id) {
        return playerRepository.deleteById(id);
    }
}
