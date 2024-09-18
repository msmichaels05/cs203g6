import React, { useState, useContext, useEffect } from "react";
import { AccountContext } from "Account";

const Status = () => {
    const [status, setStatus] = useState(false);
    const {getSession, logout} = useContext(AccountContext);

    const handleSignOut = () => {
        logout();
      };

    useEffect(() => {
        getSession()
        .then(session => {
            setStatus(true);
        });
    }, []);

    return (
        // <div></div>
        <div>
            {status? "You are logged in" : "Please login"}
        </div>
    )
};

export default Status;