import React from "react";
import { useParams } from "react-router-dom";
import initialBracket from "../data/bracketData"; // Importing the bracket data

const Match = () => {
  const { id } = useParams(); // Get match ID from the URL

  // Find the match details based on the match ID
  const match = initialBracket.find((match) => match.id === id);

  if (!match) {
    return <div>Match not found.</div>;
  }

  return (
    <div className="match-details">
      <h2>Match Details</h2>
      <p><strong>Match ID:</strong> {match.id}</p>
      <p><strong>Round:</strong> {match.tournamentRoundText}</p>
      <p><strong>Start Time:</strong> {match.startTime}</p>
      <h3>Participants:</h3>
      <ul>
        {match.participants.map((participant) => (
          <li key={participant.id}>
            {participant.name} ({participant.isWinner ? "Winner" : "Loser"}) - {participant.resultText}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Match;
