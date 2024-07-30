import React, { useEffect, useState } from "react"

const UserContext = React.createContext({ username: '', avatar: '', auth: false });

const UserProvider = ({ children }) => {
    const [user, setUser] = useState({ username: "", auth: false });

    useEffect(() => {
        if (localStorage.getItem('username') && localStorage.getItem('token')) {
            setUser((user) => ({
                username: localStorage.getItem('username'),
                avatar: '',
                auth: true,
            }));
        }
    }, []);

    const loginContext = (username, token, avatar) => {
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        setUser((user) => ({
            username: username,
            avatar: avatar,
            auth: true,
        }));
    };

    const logoutContext = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('refreshToken');
        setUser((user) => ({
            username: "",
            avatar: '',
            auth: false,
        }));
    };

    return (
        <UserContext.Provider value={{ user, setUser, loginContext, logoutContext }}>
            {children}
        </UserContext.Provider>
    );
}

export { UserContext, UserProvider }; 