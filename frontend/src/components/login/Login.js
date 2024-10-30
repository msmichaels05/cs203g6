import React, { useState, useEffect } from "react";
import { login } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import "./login.css";


const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add('login-background');

    return () => {
      document.body.classList.remove('login-background'); // Cleanup on unmount
    };
  }, []);


  

  const handleLogin = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    try {
      const response = await login(email, password);
      if (response.success) {
        navigate("/register/players");
      } else {
        setError("Invalid credentials. Please try again.");
      }
    } catch (error) {
      setError("An error occurred. Please try again later.");
    }
    setIsLoading(false);
  };

  return (
    <div className="login-container">
      <div className="tennis-ball"></div>
      <h2>Welcome Back, Player!</h2>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          <label>Email Address:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter your email"
            required
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter your password"
            required
          />
        </div>
        {error && <div className="error">{error}</div>}
        <button type="submit" disabled={isLoading}>
          {isLoading ? "Logging in..." : "Login"}
        </button>
      </form>

      {/* Links for register and guest options */}
      <div className="additional-options">
        <span onClick={() => navigate("/register")} className="link-text">
          Register here
        </span>
        <span onClick={() => navigate("/home")} className="link-text">
          Continue as Guest
        </span>
      </div>
    </div>
  );
};

export default Login;
