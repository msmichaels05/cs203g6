import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import PlayerNavbar from "../navbar/PlayerNavbar";
import { generateBracketMatches, fetchBracketMatches, updateBracketMatchWinner } from "../../api/bracketAPI";
import "./bracket.css";

const getRoundLabel = (roundIndex, totalRounds) => {
  if (roundIndex === totalRounds - 1) return "Final";
  if (roundIndex === totalRounds - 2) return "Semifinal";
  return `Round ${roundIndex + 1}`;
};

export const SingleElimination = () => {
  const { tournamentId } = useParams();
  const [bracket, setBracket] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBracketData = async () => {
      if (!tournamentId) {
        setLoading(false);
        return;
      }

      try {
        let matches = await fetchBracketMatches(tournamentId);
        if (!matches || matches.length === 0) {
          await generateBracketMatches(tournamentId);
          matches = await fetchBracketMatches(tournamentId);
        }

        if (!matches || matches.length === 0) {
          setLoading(false);
          return;
        }

        const rounds = [];
        let matchesRemaining = matches;
        while (matchesRemaining.length > 1) {
          const currentRoundMatches = matchesRemaining.slice(0, Math.ceil(matchesRemaining.length / 2));
          matchesRemaining = matchesRemaining.slice(Math.ceil(matchesRemaining.length / 2));
          rounds.push(currentRoundMatches);
        }
        rounds.push(matchesRemaining);
        setBracket(rounds);
      } catch (error) {
        console.error("Error fetching bracket data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchBracketData();
  }, [tournamentId]);

  const handleMatchClick = async (match, winner) => {
    if (!match || !winner) return;

    const updatedBracket = [...bracket];
    updatedBracket.forEach((round) => {
      round.forEach((m) => {
        if (m.id === match.id) {
          m.participants = m.participants.map((p) =>
            p && p.id === winner.id ? { ...p, isWinner: true, resultText: "Won" } : { ...p, isWinner: false, resultText: "Lost" }
          );
        }
      });
    });

    setBracket(updatedBracket);

    try {
      await updateBracketMatchWinner(tournamentId, match.id, winner.id);
    } catch (error) {
      console.error("Error updating match winner:", error);
    }
  };

  if (loading) return <div>Loading...</div>;

  const totalRounds = bracket.length;

  return (
    <div>
      <PlayerNavbar />
      <div className="bracket-container">
        {bracket.map((roundMatches, roundIndex) => (
          <div className="round" key={roundIndex}>
            <div className="round-header">{getRoundLabel(roundIndex, totalRounds)}</div>
            {roundMatches.map((match) => (
              <div className="match-container" key={match.id}>
                <div className="match">
                  <div
                    className={`participant ${
                      match.winner
                        ? match.winner.id === match.player1?.id
                          ? "winner"
                          : "loser"
                        : "pending"
                    }`}
                    onClick={() => match.player1 && handleMatchClick(match, match.player1)}
                  >
                    {match.player1 ? match.player1.name : "TBD"}
                  </div>
                  <div
                    className={`participant ${
                      match.winner
                        ? match.winner.id === match.player2?.id
                          ? "winner"
                          : "loser"
                        : "pending"
                    }`}
                    onClick={() => match.player2 && handleMatchClick(match, match.player2)}
                  >
                    {match.player2 ? match.player2.name : "TBD"}
                  </div>
                </div>
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default function App() {
  return (
    <div>
      <SingleElimination />
    </div>
  );
}
