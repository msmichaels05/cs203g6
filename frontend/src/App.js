import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/login/Login';
import Home from './components/home/home';
import Authen_home from './components/home/authen_home';
import Register from './components/register/register';
import RegisterPlayers from './components/register_players/register_players';
import Tournaments from './components/tournaments/tournaments';
import Bracket from './components/bracket/bracket';
import Profile from './components/profile/profile';
import Players from './components/players/players';
import Admins from './components/admins/admins';
import Player_tournament from './components/player_tournament/player_tournament';




import 'bootstrap/dist/css/bootstrap.min.css';



function App() {
  return (
    <Router>
      <Routes>
        {/* add more routes here */}
        <Route path="/home" element={<Home />} />
        <Route path="/authen_home" element={<Authen_home />} />


        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/register/players" element={<RegisterPlayers />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/players" element={<Players />} />
        <Route path="/admins" element={<Admins />} />
        <Route path="/tournaments" element={<Tournaments />} />
        <Route path="/player_tournament" element={<Player_tournament />} />
        <Route path="/tournament/view" element={<Bracket />} />





      </Routes>
    </Router>
  );
}

export default App;
