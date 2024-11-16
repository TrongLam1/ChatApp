'use client'

import { useState } from "react";
import avatar from '../../../assets/images/avatar.png';
import './friendComponent.scss';

export default function FriendComponent(props: any) {

    const { item, tab } = props;

    const [friend, setFriend] = useState(item);

    const handleSubscribeChannel = async (userId: string) => {

    };

    const handleSubscribeGroup = async (groupId: string) => {

    };

    const formatTime = () => {
        if (item?.lastMessage?.createAt !== undefined) {
            const dateString = item?.lastMessage?.createAt;

            const date = new Date(dateString);

            // Extract and format the components
            const day = String(date.getDate()).padStart(2, '0');
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');

            return `${day}/${month} ${hours}:${minutes}`;
        }
    };

    return (
        <div className='friend'
            onClick={() => { tab === 'friends' ? handleSubscribeChannel(item?.id) : handleSubscribeGroup(item?.groupId) }}>
            <img src={item?.avatar || item?.image_url ? item?.avatar || item?.image_url : avatar} alt='' />
            <div className='texts'>
                <div className='info'>
                    <div className='title-info'>
                        <span>{item.userName || item.groupName}</span>
                        <p>{formatTime()}</p>
                    </div>
                    {item.lastMessage &&
                        <div className='last-message'>
                            <p>{item.lastMessage.sender}:</p>
                            <p>{item.lastMessage.content}</p>
                        </div>
                    }
                </div>
            </div>
        </div>
    );
};