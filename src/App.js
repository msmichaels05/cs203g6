import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/login/Login';
import Home from './components/home/home';
import Register from './components/register/register';
import RegisterPlayers from './components/register_players/register_players';
import Tournaments from './components/tournaments/tournaments';
import TournamentsCreate from './components/tournaments_create/tournaments_create';
import TournamentUpdate from './components/tournament_update/tournament_update';
import Bracket from './components/bracket/bracket';



import 'bootstrap/dist/css/bootstrap.min.css';



function App() {
  return (
    <Router>
      <Routes>
        {/* add more routes here */}
        <Route path="/home" element={<Home />} />


        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/register_players" element={<RegisterPlayers />} />
        <Route path="/tournaments" element={<Tournaments />} />
        <Route path="/tournament/create" element={<TournamentsCreate />} />
        <Route path="/tournament/edit" element={<TournamentUpdate />} />   {/* This is Just to show how update tournaments will look like, will need back end to retireive individual tournament */}
        <Route path="/tournament/view" element={<Bracket />} />





      </Routes>
    </Router>
  );
}

export default App;
