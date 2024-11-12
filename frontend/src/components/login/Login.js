import React, { useState, useEffect } from "react";
import { loginAPI } from "../../api/loginAPI"; // Import the loginAPI function
import { useNavigate } from "react-router-dom";
import "./login.css";

const Login = () => {
  const [email, setEmail] = useState(""); // Assuming email is the username here
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
    setError(""); // Clear previous errors before making a new request

    try {
      const userInfo = await loginAPI(email, password); // Call the backend API
      if (userInfo) {
        // Assuming userInfo contains valid user data, handle success
        navigate("/register/players"); // Navigate to the next page after successful login
      } else {
        setError("Invalid credentials. Please try again.");
      }
    } catch (error) {
      setError("An error occurred. Please try again later.");
    }
    setIsLoading(false);
  };

  return (
    <div className="login-page">
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
    </div>
  );
};

export default Login;
