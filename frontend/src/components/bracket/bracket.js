import React, { useState } from "react";
import {
  SingleEliminationBracket,
  Match,
  SVGViewer,
  createTheme,
} from "@g-loot/react-tournament-brackets";
import { useLocation } from "react-router-dom";
import PlayerNavbar from '../navbar/PlayerNavbar';  // Import PlayerNavbar
import "./bracket.css";

const GlootTheme = createTheme({
  textColor: { main: "#000000", highlighted: "#F4F2FE", dark: "#707582" },
  matchBackground: { wonColor: "#2D2D59", lostColor: "#1B1D2D" },
  score: {
    background: {
      wonColor: `#10131C`,
      lostColor: "#10131C",
    },
    text: { highlightedWonColor: "#7BF59D", highlightedLostColor: "#FB7E94" },
  },
  border: {
    color: "#292B43",
    highlightedColor: "RGBA(152,82,242,0.4)",
  },
  roundHeader: { backgroundColor: "#3B3F73", fontColor: "#F4F2FE" },
  connectorColor: "#3B3F73",
  connectorColorHighlight: "RGBA(152,82,242,0.4)",
  svgBackground: "#0F121C",
});

// Initial match data
const initialBracket = [
  {
    id: "final",
    nextMatchId: null,
    tournamentRoundText: "3",
    startTime: "2021-06-01",
    state: "SCHEDULED",
    participants: [
      {
        id: "finalWinner1",
        resultText: null,
        isWinner: false,
        status: null,
        name: "-",
      },
      {
        id: "finalWinner2",
        resultText: null,
        isWinner: false,
        status: null,
        name: "-",
      },
    ],
  },
  {
    id: "semiFinal1",
    nextMatchId: "final",
    tournamentRoundText: "2",
    startTime: "2021-05-31",
    state: "SCHEDULED",
    participants: [
      {
        id: "semiFinalWinner1",
        resultText: null,
        isWinner: false,
        status: null,
        name: "Chase",
      },
      {
        id: "semiFinalLoser1",
        resultText: null,
        isWinner: false,
        status: null,
        name: "Michael",
      },
    ],
  },
  {
    id: "semiFinal2",
    nextMatchId: "final",
    tournamentRoundText: "2",
    startTime: "2021-05-31",
    state: "SCHEDULED",
    participants: [
      {
        id: "semiFinalWinner2",
        resultText: null,
        isWinner: false,
        status: null,
        name: "Jeffrey",
      },
      {
        id: "semiFinalLoser2",
        resultText: null,
        isWinner: false,
        status: null,
        name: "Thomas",
      },
    ],
  },
  {
    id: "quarterFinal1",
    nextMatchId: "semiFinal1",
    tournamentRoundText: "1",
    startTime: "2021-05-30",
    state: "SCORE_DONE",
    participants: [
      {
        id: "qWinner1",
        resultText: "Won",
        isWinner: true,
        status: "PLAYED",
        name: "Chase",
      },
      {
        id: "qLoser1",
        resultText: "Lost",
        isWinner: false,
        status: "PLAYED",
        name: "Ethan",
      },
    ],
  },
  {
    id: "quarterFinal2",
    nextMatchId: "semiFinal1",
    tournamentRoundText: "1",
    startTime: "2021-05-30",
    state: "SCORE_DONE",
    participants: [
      {
        id: "qWinner2",
        resultText: "Won",
        isWinner: true,
        status: "PLAYED",
        name: "Michael",
      },
      {
        id: "qLoser2",
        resultText: "Lost",
        isWinner: false,
        status: "PLAYED",
        name: "Nathan",
      },
    ],
  },
  {
    id: "quarterFinal3",
    nextMatchId: "semiFinal2",
    tournamentRoundText: "1",
    startTime: "2021-05-30",
    state: "SCORE_DONE",
    participants: [
      {
        id: "qWinner3",
        resultText: "Won",
        isWinner: true,
        status: "PLAYED",
        name: "Jeffrey",
      },
      {
        id: "qLoser3",
        resultText: "Lost",
        isWinner: false,
        status: "PLAYED",
        name: "David",
      },
    ],
  },
  {
    id: "quarterFinal4",
    nextMatchId: "semiFinal2",
    tournamentRoundText: "1",
    startTime: "2021-05-30",
    state: "SCORE_DONE",
    participants: [
      {
        id: "qWinner4",
        resultText: "Won",
        isWinner: true,
        status: "PLAYED",
        name: "Thomas",
      },
      {
        id: "qLoser4",
        resultText: "Lost",
        isWinner: false,
        status: "PLAYED",
        name: "William",
      },
    ],
  },
];

export const SingleElimination = () => {
  const location = useLocation();
  const { tournamentId } = location.state || {}; // Retrieve passed state
  const [bracket, setBracket] = useState(initialBracket);
  console.log("Tournament ID for bracket:", tournamentId);

  const handleMatchClick = (match, winner) => {
    const updatedBracket = bracket.map((m) => {
      if (m.id === match.id) {
        return {
          ...m,
          participants: m.participants.map((p) =>
            p.name === winner.name
              ? { ...p, isWinner: true, resultText: "Won" }
              : { ...p, isWinner: false, resultText: "Lost" }
          ),
        };
      }
      if (m.id === match.nextMatchId) {
        return {
          ...m,
          participants: [
            { ...m.participants[0], name: winner.name },
            m.participants[1],
          ],
        };
      }
      return m;
    });

    setBracket(updatedBracket);
  };

  return (
    <div>
      {/* Player Navbar */}
      <PlayerNavbar /> 

      {/* Bracket Display */}
      <div className="bracket-container">
        <SingleEliminationBracket
          theme={GlootTheme}
          matches={bracket}
          matchComponent={Match}
          svgWrapper={({ children, ...props }) => (
            <SVGViewer width={3000} height={3000} {...props}>
              {children}
            </SVGViewer>
          )}
          onMatchClick={(match) =>
            handleMatchClick(match, match.participants[0]) // For simplicity, assume first player wins
          }
          onPartyClick={(match) => console.log(match)}
        />
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


// import React, { useState, useEffect } from "react";
// import {
//   SingleEliminationBracket,
//   Match,
//   SVGViewer,
//   createTheme,
// } from "@g-loot/react-tournament-brackets";
// import { useLocation } from "react-router-dom";
// import PlayerNavbar from "../navbar/PlayerNavbar"; // Import PlayerNavbar
// import "./bracket.css";

// // Gloot Tournament Theme Customization
// const GlootTheme = createTheme({
//   textColor: { main: "#000000", highlighted: "#F4F2FE", dark: "#707582" },
//   matchBackground: { wonColor: "#2D2D59", lostColor: "#1B1D2D" },
//   score: {
//     background: { wonColor: `#10131C`, lostColor: "#10131C" },
//     text: { highlightedWonColor: "#7BF59D", highlightedLostColor: "#FB7E94" },
//   },
//   border: {
//     color: "#292B43",
//     highlightedColor: "RGBA(152,82,242,0.4)",
//   },
//   roundHeader: { backgroundColor: "#3B3F73", fontColor: "#F4F2FE" },
//   connectorColor: "#3B3F73",
//   connectorColorHighlight: "RGBA(152,82,242,0.4)",
//   svgBackground: "#0F121C",
// });

// // Fetching tournament data dynamically from the backend
// const fetchBracketData = async (tournamentId) => {
//   try {
//     const response = await fetch(`/api/tournaments/${tournamentId}/bracket`);
//     if (!response.ok) throw new Error("Failed to fetch bracket data");
//     return await response.json();
//   } catch (error) {
//     console.error("Error fetching bracket data:", error);
//     return []; // Return empty array on error
//   }
// };

// export const SingleElimination = () => {
//   const location = useLocation();
//   const { tournamentId } = location.state || {}; // Retrieve passed state
  
//   const [bracket, setBracket] = useState([]); // Empty bracket to be populated later
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   // Fetch the bracket data when the component mounts
//   useEffect(() => {
//     if (tournamentId) {
//       setLoading(true);
//       fetchBracketData(tournamentId)
//         .then((data) => {
//           setBracket(data); // Update state with fetched data
//         })
//         .catch((err) => setError(err.message))
//         .finally(() => setLoading(false));
//     }
//   }, [tournamentId]);

//   // Handle match click to update bracket state dynamically
//   const handleMatchClick = (match, winner) => {
//     const updatedBracket = bracket.map((m) => {
//       if (m.id === match.id) {
//         return {
//           ...m,
//           participants: m.participants.map((p) =>
//             p.name === winner.name
//               ? { ...p, isWinner: true, resultText: "Won" }
//               : { ...p, isWinner: false, resultText: "Lost" }
//           ),
//         };
//       }
//       if (m.id === match.nextMatchId) {
//         return {
//           ...m,
//           participants: [
//             { ...m.participants[0], name: winner.name },
//             m.participants[1],
//           ],
//         };
//       }
//       return m;
//     });

//     setBracket(updatedBracket);
//   };

//   // Loading and error states handling
//   if (loading) return <div>Loading bracket...</div>;
//   if (error) return <div>Error: {error}</div>;

//   return (
//     <div>
//       {/* Player Navbar */}
//       <PlayerNavbar />

//       {/* Bracket Display */}
//       <div className="bracket-container">
//         <SingleEliminationBracket
//           theme={GlootTheme}
//           matches={bracket}
//           matchComponent={Match}
//           svgWrapper={({ children, ...props }) => (
//             <SVGViewer width={3000} height={3000} {...props}>
//               {children}
//             </SVGViewer>
//           )}
//           onMatchClick={(match) =>
//             handleMatchClick(match, match.participants[0]) // Assume first participant wins
//           }
//           onPartyClick={(match) => console.log(match)}
//         />
//       </div>
//     </div>
//   );
// };

// export default function App() {
//   return (
//     <div>
//       <SingleElimination />
//     </div>
//   );
// }
