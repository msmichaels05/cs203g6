package com.amateuraces.highlight;

import com.amateuraces.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Highlight {
    @Id
    private HighlightKey id;
    // private Tournament tournamentOfTheMonth;
    private String tournamentOfTheMonth;
    private Player playerOfTheMonth;
    private Player mostImprovedPlayer;
    // private Match highestScoringMatch;

    public Highlight(Long year, Long month) {
        this.id = new HighlightKey(year, month);
    }

    public Highlight(HighlightKey id, Player mostImprovedPlayer, Player playerOfTheMonth, String tournamentOfTheMonth) {
        this.id = id;
        this.mostImprovedPlayer = mostImprovedPlayer;
        this.playerOfTheMonth = playerOfTheMonth;
        this.tournamentOfTheMonth = tournamentOfTheMonth;
    }
    
}