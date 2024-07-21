// import { useEffect, useRef, useState } from 'react';
// import { Client } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';

// const SOCKET_URL = 'http://localhost:8080/ws';

// const WebSocketService = (onMessageReceived) => {
//     const clientRef = useRef(null);

//     const [channel, setChannel] = useState('');

//     useEffect(() => {
//         const client = new Client({
//             webSocketFactory: () => new SockJS(SOCKET_URL),
//             // reconnectDelay: 5000,
//             // debug: (str) => { console.log(str); },
//             onConnect: (frame) => {
//                 console.log('Connected: ' + frame);
//                 client.subscribe('/channel/private/' + channel, (msg) => {
//                     const message = JSON.parse(msg.body);
//                     onMessageReceived(message);
//                 });
//             }
//         });

//         client.activate();
//         clientRef.current = client;

//         return () => {
//             client.deactivate();
//         };
//     }, [onMessageReceived]);

//     const sendMessage = (message) => {
//         if (clientRef.current) {
//             clientRef.current.publish({ destination: '/app/message', body: JSON.stringify(message) });
//         }
//     };

//     return { sendMessage };
// };

// export default WebSocketService;



