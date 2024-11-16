'use client'

import { useState } from "react";
import avatar from '../../assets/images/avatar.png';
import './chatComponent.scss';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleInfo, faPhone, faVideo } from "@fortawesome/free-solid-svg-icons";

export default function ChatComponent(props: any) {

    const { tab, receiver, description } = props;

    const [open, setOpen] = useState(false);

    const [message, setMessage] = useState('');
    const [chatMessages, setChatMessages] = useState([]);
    const [userChat, setUserChat] = useState();

    const handleSendMessage = async () => { }

    const handleSubmitMessage = async (e) => { }

    return (
        <>
            <div className='chat-container col-lg-8'>
                {receiver &&
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
                                <FontAwesomeIcon icon={faPhone} />
                                <FontAwesomeIcon icon={faVideo} />
                                <FontAwesomeIcon icon={faCircleInfo} />
                            </div>
                        </div>

                        {/* <div className='center'>
                            {chatMessages && chatMessages.length > 0 &&
                                chatMessages.map((message, index) => {
                                    return (<Message message={message} key={`message-${index}`} tab={tab} />)
                                })
                            }
                            <div ref={messagesEndRef} />
                        </div> */}

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
                }
            </div>
            {/* <UploadImage open={open} setOpen={setOpen}
                subscribe={subscribe}
                handleGetMessage={handleGetMessage}
                tab={tab}
            /> */}
        </>
    );
} 