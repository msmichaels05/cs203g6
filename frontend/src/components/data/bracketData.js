const initialBracket = [
    {
      id: "final",
      nextMatchId: null,
      tournamentRoundText: "Final",
      startTime: "2024-05-30",
      state: "SCHEDULED",
      participants: [
        {
          id: "1",
          resultText: "Won",
          isWinner: true,
          status: "PLAYED",
          name: "Chase",
        },
        {
          id: "2",
          resultText: "Lost",
          isWinner: false,
          status: "PLAYED",
          name: "Michael",
        },
      ],
    },
    {
      id: "19754",
      nextMatchId: "final",
      tournamentRoundText: "Semi-Final",
      startTime: "2024-05-30",
      state: "SCORE_DONE",
      participants: [
        {
          id: "3",
          resultText: "Won",
          isWinner: true,
          status: "PLAYED",
          name: "Jeffrey",
        },
        {
          id: "4",
          resultText: "Lost",
          isWinner: false,
          status: "PLAYED",
          name: "Thomas",
        },
      ],
    },
    // Add more match data here...
  ];
  
  export default initialBracket;
  