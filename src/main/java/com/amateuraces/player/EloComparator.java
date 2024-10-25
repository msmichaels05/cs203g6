package com.amateuraces.player;

import java.util.Comparator;

public class EloComparator implements Comparator<Player> { //Sort players by elo in descending order
    @Override
    public int compare(Player p1, Player p2) {
        if (p1.getElo() > p2.getElo()) {
            return 1;
        }
        else if (p2.getElo() > p1.getElo()) {
            return -1;
        }
        return 0;
    }
}
