import { useEffect, useContext } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { AccountContext } from 'Account';
import { axiosPrivate } from "api/axios";
import useAuth from "./useAuth"

const useAxiosPrivate = () => {
    const { getSession, logout } = useContext(AccountContext);
    // const { getSession, logout } = useAuth();
    const navigate = useNavigate();

   useEffect(() => {
        // Setting up the request interceptor
        const requestIntercept = axiosPrivate.interceptors.request.use(
            async config => {
                // Get the session and modify the config with the token
                const session = await getSession();
                if (!config.headers['Authorization']) {
                    config.headers['Authorization'] = `${session.accessToken.jwtToken}`;
                }
                return config;
            },
            error => Promise.reject(error)
        ); 

        // Setting up the response interceptor
        const responseIntercept = axiosPrivate.interceptors.response.use(response => response, async error => {
            const status = error.response ? error.response.status : null;
            if (status === 403) {
                logout(); 
                navigate('/authentication/sign-in');
            }
            return Promise.reject(error);
        });

        // Clean up function to eject interceptors
        return () => {
            axiosPrivate.interceptors.request.eject(requestIntercept);
            axiosPrivate.interceptors.response.eject(responseIntercept);
        };
    }, [logout, navigate]);

    return axiosPrivate;
};

export default useAxiosPrivate;
