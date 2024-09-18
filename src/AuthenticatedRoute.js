// AuthenticatedRoute.js
import React, { useContext, useEffect, useState } from 'react';
import { Outlet } from "react-router-dom";
import { useLocation, Navigate, useNavigate } from 'react-router-dom';
import { AccountContext } from './Account';
import PropTypes from "prop-types";

const AuthenticatedRoute = ({  }) => {
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const { getSession } = useContext(AccountContext);
  const navigate = useNavigate(); // Using the useNavigate hook
  const location = useLocation();

  useEffect(() => {
    getSession()
      .then(session => {
        // console.log('Session:', session.accessToken.jwtToken);
        setIsAuthenticated(true);
      })
      .catch(() => {
        setIsAuthenticated(false);
        navigate('/authentication/sign-in', { replace: true }); 
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [getSession]);

  if (isLoading) {
    return <div>Loading...</div>; // Or any loading indicator you prefer
  }

  return isAuthenticated ? <Outlet/> : <Navigate to="/authentication/sign-in" state={{ from: location }} replace/>;
};

export default AuthenticatedRoute;
