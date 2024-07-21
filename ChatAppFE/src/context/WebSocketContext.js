import React, { createContext, useContext, useRef, useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const WebSocketContext = React.createContext({
    subscribe: '', receiver: '', messageReceiver: '', description: ''
});

const SOCKET_URL = 'http://localhost:8080/ws';

const WebSocketProvider = ({ children }) => {
    const clientRef = useRef(null);
    const [subscribe, setSubscribe] = useState('');
    const [receiver, setReceiver] = useState('');
    const [description, setDescription] = useState('');
    const [messageReceiver, setMessageReceiver] = useState({
        sender: '',
        content: '',
        createAt: ''
    });

    useEffect(() => {
        if (subscribe === '') return;

        const client = new Client({
            webSocketFactory: () => new SockJS(SOCKET_URL),
            onConnect: (frame) => {
                console.log('Connected: ' + frame);
                client.subscribe('/channel/private/' + subscribe, (msg) => {
                    const message = JSON.parse(msg.body);
                    setMessageReceiver(prevState => ({
                        ...prevState,
                        ...message
                    }));
                });
            }
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [subscribe]);

    const connectToChannel = (newChannel) => {
        setSubscribe(newChannel);
    };

    const handleSetReceiver = (receiver) => {
        setReceiver(receiver);
    };

    const handleSetDescription = (description) => {
        setDescription(description);
    };

    const sendMessage = (message) => {
        if (clientRef.current) {
            clientRef.current.publish({ destination: '/app/message', body: JSON.stringify(message) });
        }
    };

    return (
        <WebSocketContext.Provider value={{ subscribe, receiver, messageReceiver, description, connectToChannel, handleSetReceiver, handleSetDescription, setMessageReceiver }}>
            {children}
        </WebSocketContext.Provider>
    );
};

export { WebSocketContext, WebSocketProvider };
