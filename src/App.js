import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/login/Login';
import Home from './components/home/home';
import Register from './components/register/register';
import RegisterPlayers from './components/register_players/register_players';


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


      </Routes>
    </Router>
  );
}

export default App;
