import React, { useState } from 'react';
import './tournaments.css'; // Importing CSS for styling

const tournaments = [
  { id: 1, name: "Champions Cup", ELOrequirement: 1500 },
  { id: 2, name: "Elite Tournament", ELOrequirement: 1600 },
  { id: 3, name: "Amateur Showdown", ELOrequirement: 1400 },
];

const isAdmin = true; // Change this flag to false for player view

const Tournament = () => {
  return (
    <div>
      <div className="navbar">
        <h1>All Tournaments - AmateurAces</h1>
      </div>

      <div className="container">
        {/* Button to add a new tournament (visible only for admins) */}
        {isAdmin && (
          <a href="/tournament/create" className="add-tournament-btn">
            Add New Tournament
          </a>
        )}

        {/* Tournament Grid */}
        <div className="tournament-grid">
          {tournaments.map((tournament) => (
            <div key={tournament.id} className="tournament-card">
              <img src="https://via.placeholder.com/300x200" alt="Tournament" />
              <h3>{tournament.name}</h3>
              <p>
                <strong>ELO Requirement:</strong> {tournament.ELOrequirement}
              </p>

              <div className="btn-container">
                {/* Show different buttons based on the user's role */}
                {isAdmin ? (
                  <>
                    <a href={`/tournament/view/${tournament.id}`} className="btn view-btn">
                      View
                    </a>
                    <a href={`/tournament/edit/${tournament.id}`} className="btn edit-btn">
                      Edit
                    </a>
                    <form action={`/tournament/delete/${tournament.id}`} method="POST">
                      <button type="submit" className="btn delete-btn">
                        Delete
                      </button>
                    </form>
                  </>
                ) : (
                  <form action={`/tournament/register/${tournament.id}`} method="GET">
                    <button type="submit" className="btn register-btn">
                      Register
                    </button>
                  </form>
                )}
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        <div className="pagination">
          <a href="#">&laquo;</a>
          <a href="#" className="active">1</a>
          <a href="#">2</a>
          <a href="#">3</a>
          <a href="#">&raquo;</a>
        </div>
      </div>
    </div>
  );
};

export default Tournament;
