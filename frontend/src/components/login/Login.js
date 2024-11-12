import React, { useState, useEffect } from "react";
import { loginAPI } from "../../api/loginAPI"; // Import the loginAPI function
import { useNavigate } from "react-router-dom";
import "./login.css";

const Login = () => {
  const [username, setUsername] = useState(""); // Changed from email to username
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add("login-background");

    return () => {
      document.body.classList.remove("login-background"); // Cleanup on unmount
    };
  }, []);

  const handleLogin = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(""); // Clear previous errors before making a new request
  
    try {
      const userInfo = await loginAPI(username, password); // Call the backend API with username
      if (userInfo) {
        // Assuming userInfo contains valid user data and a token, handle success
        localStorage.setItem("token", userInfo.token); // Store token for session management
        navigate("/authen_home"); // Navigate to the authen_home page after successful login
      } else {
        setError("Invalid credentials. Please try again.");
      }
    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        setError(error.response.data.message); // Show specific backend message
      } else {
        setError("An error occurred. Please try again later.");
      }
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
            <label>Username:</label> {/* Changed label to Username */}
            <input
              type="text" // Changed from email type to text
              value={username}
              onChange={(e) => {
                setUsername(e.target.value);
                setError(""); // Clear error on input change
              }}
              placeholder="Enter your username"
              required
              disabled={isLoading} // Disable input while loading
            />
          </div>
          <div className="form-group">
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                setError(""); // Clear error on input change
              }}
              placeholder="Enter your password"
              required
              disabled={isLoading} // Disable input while loading
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
          <span onClick={() => navigate("/")} className="link-text">
            Continue as Guest
          </span>
        </div>
      </div>
    </div>
  );
};

export default Login;
