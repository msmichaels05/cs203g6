package com.amateuraces.highlight;

import com.amateuraces.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    // private Tournament tournamentOfTheMonth;
    private String tournamentOfTheMonth;
    @ManyToOne // Assuming you'll have a many-to-one relationship with Player
    @JoinColumn(name = "player_of_the_month_id")
    private Player playerOfTheMonth;

    @ManyToOne // Assuming you'll also have a many-to-one relationship for most improved player
    @JoinColumn(name = "most_improved_player_id")
    private Player mostImprovedPlayer;
    // private Player playerOfTheMonth;
    // private Player mostImprovedPlayer;
    // private Match highestScoringMatch;

    public Highlight(Player mostImprovedPlayer, Player playerOfTheMonth, String tournamentOfTheMonth) {
        this.mostImprovedPlayer = mostImprovedPlayer;
        this.playerOfTheMonth = playerOfTheMonth;
        this.tournamentOfTheMonth = tournamentOfTheMonth;
    }

    public Highlight(String tournamentOfTheMonth) {
        this.tournamentOfTheMonth = tournamentOfTheMonth;
    }
    
}