import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PlayerNavbar from './components/navbar/PlayerNavbar';  // Import PlayerNavbar
import AdminNavbar from './components/navbar/AdminNavbar'; // Import AdminNavbar
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

import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [role, setRole] = useState(null); // State variable to store the role

  useEffect(() => {
    // Fetch user role from backend
    const fetchUserRole = async () => {
      try {
        const response = await axios.get('/api/user/role');
        setRole(response.data); // Store the role in state
      } catch (error) {
        console.error("Error fetching user role:", error);
      }
    };

    fetchUserRole();
  }, []);

  console.log("User Role:", role); // This will log the role to the console

  return (
    <Router>
      {role === 'ROLE_ADMIN' ? <AdminNavbar /> : role === 'ROLE_USER' && <PlayerNavbar />}
      
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/authen_home" element={<Authen_home role={role} />} /> {/* Pass role as prop */}
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/register/players" element={<RegisterPlayers />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/players" element={<Players role={role} />} /> {/* Pass role as prop */}
        <Route path="/admins" element={<Admins role={role} />} /> {/* Pass role as prop */}
        <Route path="/tournaments" element={<Tournaments />} />
        <Route path="/tournament/view" element={<Bracket />} />
      </Routes>
    </Router>
  );
}

export default App;
