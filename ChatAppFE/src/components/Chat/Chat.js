import './Chat.scss';
import avatar from '../../assets/image/avatar.jpg';
import bg from '../../assets/image/galaxy.webp';
import { useEffect, useState, useRef, useContext } from "react";
import WebSocketService from '../../services/WebSocketService';
import { sendMessage as sendApiMessage, getMessages } from '../../services/ChatService';
import Message from '../Message/Message';
import { sendMessageToChannel, fetchListMessagesFromChannel } from '../../services/ChannelService';
import { WebSocketContext } from '../../context/WebSocketContext';
import { sendMessageToGroup, fetchListMessagesFromGroup } from '../../services/GroupService';

const Chat = (props) => {

    const { tab, setChatWith } = props;
    const { subscribe, receiver, messageReceiver,
        description, handleSetReceiver, setMessageReceiver } = useContext(WebSocketContext);

    const [message, setMessage] = useState('');
    const [chatMessages, setChatMessages] = useState([]);

    const messagesEndRef = useRef(null);

    useEffect(() => {

    }, [chatMessages.length]);

    useEffect(() => {
        handleSetReceiver(null);
        setChatWith(null);
    }, [tab]);

    useEffect(() => {
        if (subscribe !== "") {
            handleGetMessage();
        }
    }, [subscribe]);

    useEffect(() => {
        scrollToBottom();
    }, [chatMessages.length]);

    useEffect(() => {
        handleGetMessage();
        setMessageReceiver({
            sender: '',
            content: '',
            createAt: ''
        });
    }, [messageReceiver.content]);

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
        <div className='chat-container col-lg-8'>
            {receiver &&
                <>
                    <div className='top'>
                        <div className='user'>
                            <img src={avatar} alt='' />
                            <div className='texts'>
                                <span>{receiver}</span>
                                {tab === 'groups' && <span>{description} members</span>}
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
                    </div>

                    <div className='bottom'>
                        <div className='icons'>
                            <i className="fa-regular fa-image"></i>
                            <i className="fa-solid fa-camera"></i>
                            <i className="fa-solid fa-microphone"></i>
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
            }
        </div>
    );
};

export default Chat;