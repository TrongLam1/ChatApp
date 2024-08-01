import React from 'react';
import './Chat.scss';
import avatar from '../../assets/image/avatar.jpg';
import { useEffect, useState, useRef, useContext } from "react";
import { toast } from 'react-toastify';
import Message from '../Message/Message';
import UploadImage from '../Modal/UploadImage/UploadImage';
import { sendMessageToChannel, fetchListMessagesFromChannel } from '../../services/ChannelService';
import { WebSocketContext } from '../../context/WebSocketContext';
import { sendMessageToGroup, fetchListMessagesFromGroup } from '../../services/GroupService';

const Chat = (props) => {

    const { tab, setChatWith } = props;
    const { subscribe, receiver, messageReceiver, notifyReceive, description, handleSetReceiver, setMessageReceiver, setNotifyReceive, setDescription } = useContext(WebSocketContext);

    const [open, setOpen] = useState(false);

    const [message, setMessage] = useState('');
    const [chatMessages, setChatMessages] = useState([]);

    const messagesEndRef = useRef(null);

    useEffect(() => {
        handleSetReceiver(null);
        setChatWith(null);
        setDescription(null);
        setChatMessages([]);
    }, [tab]);

    useEffect(() => {
        if (subscribe) {
            handleGetMessage();
        }
    }, [subscribe]);

    useEffect(() => {
        scrollToBottom();
    }, [chatMessages]);

    useEffect(() => {
        if (messageReceiver.content) {
            setChatMessages(prevMessages => [...prevMessages, messageReceiver]);
            scrollToBottom();
            setMessageReceiver({ sender: '', content: '', image_url: '', createAt: '' });
        }
    }, [messageReceiver]);

    useEffect(() => {
        if (notifyReceive.sender !== '' && notifyReceive.sender !== receiver) {
            toast.success("New message from: " + notifyReceive.sender);
            setNotifyReceive({ sender: '', content: '', createAt: '' });
        }
    }, [notifyReceive]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const handleGetMessage = async () => {
        const res = tab === 'friends' ? await fetchListMessagesFromChannel(subscribe) : await fetchListMessagesFromGroup(subscribe);
        if (res && res.status === 200) {
            setChatMessages(res.data);
        }
    };

    const handleSendMessage = async () => {
        const chatMessage = {
            "subscribe": subscribe,
            "content": message,
        };
        const res = tab === 'friends' ? await sendMessageToChannel(chatMessage)
            : await sendMessageToGroup(chatMessage);
        setMessage('');
        handleGetMessage();
    };

    const handleSubmitMessage = async (e) => {
        if (e.key === "Enter") {
            await handleSendMessage();
        }
    };

    return (
        <>
            <div className='chat-container col-lg-8'>
                {/* {receiver &&
                    
                } */}
                <>
                    <div className='top'>
                        <div className='user'>
                            <img src={avatar} alt='avatar' />
                            <div className='texts'>
                                <span className='receiver'>{receiver}</span>
                                {tab === 'groups' && description && <span>{description} members</span>}
                            </div>
                        </div>
                        <div className='icons'>
                            <i className="fa-solid fa-phone"></i>
                            <i className="fa-solid fa-video"></i>
                            <i className="fa-solid fa-circle-info"></i>
                        </div>
                    </div>

                    <div className='center'>
                        {chatMessages && chatMessages.length > 0 &&
                            chatMessages.map((message, index) => {
                                return (<Message message={message} key={`message-${index}`} tab={tab} />)
                            })
                        }
                        <div ref={messagesEndRef} />
                    </div>

                    <div className='bottom'>
                        <div className='icons'>
                            <i className="fa-regular fa-image" onClick={() => setOpen(!open)}></i>
                        </div>
                        <input type='text' placeholder='Type a message...' value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            onKeyDown={(e) => handleSubmitMessage(e)}
                        />
                        <button className='send-button'
                            onClick={handleSendMessage}
                        >
                            Send
                        </button>
                    </div>
                </>
            </div>
            <UploadImage open={open} setOpen={setOpen}
                subscribe={subscribe}
                handleGetMessage={handleGetMessage}
                tab={tab}
            />
        </>
    );
};

export default Chat;