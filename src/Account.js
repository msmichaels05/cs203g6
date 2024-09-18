import React, { createContext, useState, useEffect } from "react";
import { CognitoUser, AuthenticationDetails } from "amazon-cognito-identity-js";
import Pool from "UserPool";
import PropTypes from 'prop-types';
import { useNavigate } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';

// Create context with default value
const AccountContext = createContext({
    authenticate: async () => { },
    getSession: async () => { },
    logout: () => { },
});

const Account = (props) => {
    const navigate = useNavigate();
    // const [cognitoUsername, setCognitoUsername] = useState("Guest"); // State to store Cognito username
    // const [isAuthenticating, setIsAuthenticating] = useState(true);

    // Function to decode JWT using jwt-decode
    // const decodeJWT = (token) => {
    //     try {
    //         const decoded = jwtDecode(token);
    //         return decoded;
    //     } catch (error) {
    //         console.error('Failed to decode JWT:', error);
    //         return null;
    //     }
    // };

    // // Function to extract Cognito username from decoded JWT
    // const getCognitoUsernameFromJWT = (token) => {
    //     const decoded = decodeJWT(token);
    //     if (decoded && decoded['username']) {
    //         return decoded['username'];
    //     }
    //     return null;
    // };

    // const getUsername = async () => {
    //     return new Promise((resolve, reject) => {
    //         const user = Pool.getCurrentUser();
    //         if (user) {
    //             console.log("test")
    //             user.getSession((err, session) => {
    //                 if (err) {
    //                     console.error('Failed to get the session for username retrieval.', err);
    //                     reject("Error retrieving session"); // Reject the promise on error
    //                 } else if (session.isValid()) {
    //                     const token = session.getIdToken().getJwtToken();
    //                     const decoded = decodeJWT(token);
    //                     if (decoded && decoded.username) {
    //                         resolve(decoded.username); // Resolve the promise with the username
    //                     } else {
    //                         console.log("No username found in token.");
    //                         reject("Username not found in token"); // Reject if username not found
    //                     }
    //                 } else {
    //                     console.log('Session is invalid.');
    //                     reject("Invalid session"); // Reject on invalid session
    //                 }
    //             });
    //         } else {
    //             console.log('No current user for getUsername.');
    //             reject("No current user"); // Reject if no current user
    //         }
    //     });
    // };

    const getSession = async () => {
        return new Promise((resolve, reject) => {
            const user = Pool.getCurrentUser();
            if (user) {
                user.getSession((err, session) => {
                    if (err) {
                        console.log('Failed to get the session.', err);
                        reject('Failed to get the session.'); // Reject with custom message
                        user.signOut();
                        navigate('/authentication/sign-in', { replace: true });
                    } else {
                        if (session.isValid()) {
                            resolve(session);
                        } else {
                            console.log('Session is invalid.');
                            reject('Session is invalid.'); // Reject with custom message
                        }
                    }
                });
            } else {
                console.log('No current user.');
                reject('No current user.'); // Reject with custom message
            }
        });
    };

    const authenticate = async (Username, Password) => {
        return await new Promise((resolve, reject) => {
            const user = new CognitoUser({ Username, Pool });

            const authDetails = new AuthenticationDetails({ Username, Password });

            user.authenticateUser(authDetails, {
                onSuccess: (data) => {
                    console.log("onSuccess: ", data);
                    resolve(data);
                    // console.log(data.accessToken.jwtToken);
                    // const token = data.accessToken.jwtToken;
                    // const cognitoUsername = getCognitoUsernameFromJWT(token);
                    // setCognitoUsername(cognitoUsername); 
                    // console.log(cognitoUsername);
                    navigate("/");
                },
                onFailure: (err) => {
                    console.log(authDetails)
                    console.log("onFailure: ", err);
                    reject(err);
                },
                newPasswordRequired: (data) => {
                    console.log("newPasswordRequired: ", data);
                    resolve(data);
                }
            })
        });
    }

    const logout = () => {
        const user = Pool.getCurrentUser();
        if (user) {
            user.signOut();
            // setCognitoUsername(null); // Clear Cognito username from state upon logout
        }
    }


    // useEffect(() => {
    //     // Attempt to get the session on initial render/load.
    //     getSession().then(() => setIsAuthenticating(false)).catch(() => setIsAuthenticating(false));
    // }, []);

    return (
        <AccountContext.Provider value={{ authenticate, getSession, logout }}>
            {props.children}
        </AccountContext.Provider>
    )
}

Account.propTypes = {
    children: PropTypes.node // Add this line to include children prop validation
};

export { Account, AccountContext };
