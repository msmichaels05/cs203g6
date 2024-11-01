package com.amateuraces.player;

import java.util.Comparator;

public class EloComparator implements Comparator<Player> { //Sort players by elo in descending order
    @Override
    public int compare(Player p1, Player p2) {
        return Integer.compare(p2.getElo(), p1.getElo());
    }
}
