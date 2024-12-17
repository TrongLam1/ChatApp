'use client'

import { ChannelGetMessages, ChannelSendMessage, GroupGetMessages, GroupSendMessage } from "@/app/api/messageApi";
import avatarDefault from '@/assets/images/avatar.png';
import group from '@/assets/images/group.png';
import { useContactObject } from "@/providers/contactObjectProvider";
import { faCircleInfo, faImage, faPhone, faVideo } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Image from "next/image";
import { useEffect, useRef, useState } from "react";
import { toast } from "react-toastify";
import MessageComponent from "../message/messageComponent";
import './chatComponent.scss';
import { io } from "socket.io-client";
import UploadImageModal from "../modal/uploadImage/uploadImageModal";

const socket = io('http://localhost:3001');

export default function ChatComponent(props: any) {

    const { token, user } = props;
    const { contactObject } = useContactObject();

    const [avatar, setAvatar] = useState<any>();
    const [open, setOpen] = useState<boolean>(false);
    const [subscribe, setSubscribe] = useState<string>();
    const [message, setMessage] = useState<any>();
    const [chatMessages, setChatMessages] = useState<any>([]);

    const messagesEndRef = useRef(null);

    useEffect(() => {
        scrollToBottom();
    }, [chatMessages]);

    useEffect(() => {
        if (contactObject) {
            handleGetMessages();
            if (contactObject.isGroup) {
                setAvatar(group);
            } else {
                setAvatar(contactObject.avatar ? contactObject.avatar : avatarDefault);
            }
        }
    }, [contactObject]);

    useEffect(() => {
        socket.emit('joinChat', subscribe);

        socket.on('newMessage', (message) => {
            if (message.subscribeId === subscribe) {
                setChatMessages((prev: any) => [...prev, message]);
            }
        });

        return () => {
            socket.off('newMessage');
        };
    }, [subscribe]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const handleGetMessages = async () => {
        if (contactObject.isGroup) {
            const res = await GroupGetMessages(token, contactObject.id);
            if (res.statusCode === 200) {
                setChatMessages(res.data);
                setSubscribe(contactObject.id);
            }
        } else {
            const res = await ChannelGetMessages(token, contactObject.channelId);
            if (res.statusCode === 200) {
                setChatMessages(res.data);
                setSubscribe(contactObject.channelId);
            }
        }
    };

    const handleChannelSendMessage = async () => {
        const body = {
            id: contactObject.channelId,
            content: message
        };

        const res = await ChannelSendMessage(token, body);
        if (res.statusCode === 201) {
            setMessage('');
        } else { toast.error(res.message); }
    }

    const handleGroupSendMessage = async () => {
        const body = {
            id: contactObject.id,
            content: message
        };

        const res = await GroupSendMessage(token, body);
        if (res.statusCode === 201) {
            setMessage('');
        } else { toast.error(res.message); }
    }

    const handleSubmitMessage = async () => {
        if (contactObject.isGroup) {
            await handleGroupSendMessage();
        } else {
            await handleChannelSendMessage();
        }
    }

    return (
        <>
            <div className='chat-container col-lg-8'>
                {contactObject &&
                    <>
                        <div className='top'>
                            <div className='user'>
                                <div className="avatar">
                                    <Image src={contactObject.isGroup ? group : avatar} fill alt='avatar' />
                                </div>
                                <div className='texts'>
                                    <span className='receiver'>{contactObject.name}</span>
                                    {contactObject.isGroup &&
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

                        <div className='center'>
                            {chatMessages && chatMessages.length > 0 &&
                                chatMessages.map((message: any, index: number) => {
                                    return (<MessageComponent user={user}
                                        message={message} key={`message-${index}`} />)
                                })
                            }
                            <div ref={messagesEndRef} />
                        </div>

                        <div className='bottom'>
                            <div className='icons'>
                                <FontAwesomeIcon icon={faImage} onClick={() => setOpen(!open)} />
                            </div>
                            <input type='text' placeholder='Type a message...' value={message}
                                onChange={(e) => setMessage(e.target.value)}
                            />
                            <button className='send-button'
                                onClick={handleSubmitMessage}
                            >
                                Gửi
                            </button>
                        </div>
                    </>
                }
            </div>
            {open && <UploadImageModal setOpen={setOpen} token={token} />}
        </>
    );
} 