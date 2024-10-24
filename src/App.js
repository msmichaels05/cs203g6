import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/login/Login';
import Home from './components/home/home';
import 'bootstrap/dist/css/bootstrap.min.css';



function App() {
  return (
    <Router>
      <Routes>
        {/* add more routes here */}
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;
