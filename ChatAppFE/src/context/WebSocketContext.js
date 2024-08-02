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
    const [channelNotify, setChannelNotify] = useState('');
    const [receiver, setReceiver] = useState('');
    const [description, setDescription] = useState('');
    const [messageReceiver, setMessageReceiver] = useState({
        sender: '',
        content: '',
        createAt: ''
    });
    const [notifyReceive, setNotifyReceive] = useState({
        sender: '',
        content: '',
        createAt: ''
    });
    const [notifyAddFriend, setNotifyAddFriend] = useState('');

    useEffect(() => {
        if (channelNotify === '') return;

        const client = new Client({
            webSocketFactory: () => new SockJS(SOCKET_URL),
            onConnect: (frame) => {
                console.log('Connected: ' + frame);

                // Subscribe to the notification channel
                if (channelNotify !== '') {
                    client.subscribe(`/channel/notify/${channelNotify}`, (msg) => {
                        const message = JSON.parse(msg.body);
                        setNotifyReceive(prevState => ({
                            ...prevState,
                            ...message
                        }));
                    });
                }

                if (channelNotify !== '') {
                    client.subscribe(`/channel/notify/add-friend/${channelNotify}`, (msg) => {
                        const message = msg.body;
                        setNotifyAddFriend(message);
                    });
                }

                // Subscribe to the chatting channel if `subscribe` is set
                if (subscribe !== '') {
                    client.subscribe(`/channel/private/${subscribe}`, (msg) => {
                        const message = JSON.parse(msg.body);
                        setMessageReceiver(prevState => ({
                            ...prevState,
                            ...message
                        }));
                    });
                }
            }
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [subscribe, channelNotify]);

    const connectToChannel = (newChannel) => {
        setSubscribe(newChannel);
    };

    const handleSetReceiver = (receiver) => {
        setReceiver(receiver);
    };

    const handleSetDescription = (description) => {
        setDescription(description);
    };

    return (
        <WebSocketContext.Provider value={{ subscribe, receiver, channelNotify, messageReceiver, description, notifyReceive, notifyAddFriend, setDescription, connectToChannel, handleSetReceiver, handleSetDescription, setMessageReceiver, setChannelNotify, setNotifyReceive, setNotifyAddFriend }}>
            {children}
        </WebSocketContext.Provider>
    );
};

export { WebSocketContext, WebSocketProvider };
