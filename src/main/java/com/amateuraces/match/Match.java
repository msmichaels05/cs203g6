package com.amateuraces.match;

import java.util.ArrayList;
import java.util.List;

import com.amateuraces.player.Player;
import com.amateuraces.tournament.Tournament;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", insertable = false, updatable = false)
    private Tournament tournament;

    @OneToOne
    @JoinColumn(name = "matchPlayer1")
    private Player player1;

    @OneToOne
    @JoinColumn(name = "matchPlayer2")
    private Player player2;

    @OneToOne
    @JoinColumn(name = "matchWinner")
    private Player winner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_match_id")
    private Match parentMatch = null;

    @OneToOne(mappedBy = "parentMatch", cascade = CascadeType.ALL)
    private Match leftChild = null;

    @OneToOne(mappedBy = "parentMatch", cascade = CascadeType.ALL)
    private Match rightChild = null;

    private int roundNumber;

    public Match(Tournament tournament, Player player1, Player player2) {
        this.tournament = tournament;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setPlayer1(Player newPlayer) {
        player1 = newPlayer;
    }

    public void setPlayer2(Player newPlayer) {
        player2 = newPlayer;
    }

    public void setLeftChild(Match leftChild) {
        this.leftChild = leftChild;
        if (leftChild != null) {
            leftChild.setParentMatch(this);
        }
    }
    
    public void setRightChild(Match rightChild) {
        this.rightChild = rightChild;
        if (rightChild != null) {
            rightChild.setParentMatch(this);
        }
    }
    

    public List<Match> getChildMatches() {
        List<Match> children = new ArrayList<>();
        if (leftChild != null) {
            children.add(leftChild);
        }
        if (rightChild != null) {
            children.add(rightChild);
        }
        return children;
    }

    public void setParentMatch(Match match) {
        this.parentMatch = match;
        // Ensure bidirectional relationship
        if (match != null) {
            if (match.getLeftChild() == null) {
                match.setLeftChild(this);
            } else if (match.getRightChild() == null) {
                match.setRightChild(this);
            } else {
                // Handle the case where both child slots are occupied
                throw new IllegalStateException("Parent match already has two child matches.");
            }
        }
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
