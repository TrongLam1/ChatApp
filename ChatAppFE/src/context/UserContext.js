import React, { useEffect, useState } from "react"

const UserContext = React.createContext({ username: '', auth: false });

const UserProvider = ({ children }) => {
    const [user, setUser] = useState({ username: "", auth: false });

    useEffect(() => {
        if (localStorage.getItem('username') && localStorage.getItem('token')) {
            setUser((user) => ({
                username: localStorage.getItem('username'),
                auth: true,
            }));
        }
    }, []);

    const loginContext = (username, token) => {
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        setUser((user) => ({
            username: username,
            auth: true,
        }));
    };

    const logoutContext = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('refreshToken');
        setUser((user) => ({
            username: "",
            auth: false,
        }));
    };

    return (
        <UserContext.Provider value={{ user, loginContext, logoutContext }}>
            {children}
        </UserContext.Provider>
    );
}

export { UserContext, UserProvider }; 