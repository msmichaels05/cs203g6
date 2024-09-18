// import { useContext, useDebugValue } from "react";
import { useContext, useState, useEffect } from "react";
// import AuthContext from "../context/AuthProvider";
import { AccountContext } from 'Account'; // Adjust the path as needed

const useAuth = () => {
    const { getSession, logout } = useContext(AccountContext);
    const [token, setToken] = useState(null);

    // getSession()
    // .then(session => {
    //     setToken(session.accessToken.jwtToken)
    // });


    return { getSession, logout };
}

export default useAuth;