'use client'

import { useEffect, useState } from "react";
import avatar from '../../assets/images/avatar.png';
import './chatComponent.scss';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleInfo, faImage, faPhone, faVideo } from "@fortawesome/free-solid-svg-icons";
import Image from "next/image";
import { useContactObject } from "@/providers/contactObjectProvider";
import { useTab } from "@/providers/tabProvider";

export default function ChatComponent(props: any) {

    const { tab } = useTab();
    const { contactObject } = useContactObject();

    const [open, setOpen] = useState(false);

    const [message, setMessage] = useState('');
    const [chatMessages, setChatMessages] = useState([]);

    useEffect(() => { }, [contactObject]);

    const handleSendMessage = async () => { }

    const handleSubmitMessage = async (e) => { }

    return (
        <>
            <div className='chat-container col-lg-8'>
                {contactObject &&
                    <>
                        <div className='top'>
                            <div className='user'>
                                <Image src={avatar} alt='avatar' />
                                <div className='texts'>
                                    <span className='receiver'>{contactObject.name}</span>
                                    {tab === 'groups' &&
                                        <span>{contactObject.members} members</span>
                                    }
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
                                <FontAwesomeIcon icon={faImage} onClick={() => setOpen(!open)} />
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