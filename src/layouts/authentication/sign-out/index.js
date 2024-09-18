// SignOut.js
import React, { useContext } from 'react';
import { AccountContext } from 'Account';
import { useNavigate } from 'react-router-dom';
import './sign-out.css'; 

function SignOut() {
  const { logout } = useContext(AccountContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/authentication/sign-in');
  };

  const handleCancel = () => {
    navigate('/user'); 
  };

  return (
    <div className="signout-container">
      <div className="signout-dialog">
        <p>Are you sure you want to log out?</p>
        <div className="button-group">
          <button onClick={handleLogout} className="button-yes">Yes</button>
          <button onClick={handleCancel} className="button-no">No</button>
        </div>
      </div>
    </div>
  );
}

export default SignOut;
