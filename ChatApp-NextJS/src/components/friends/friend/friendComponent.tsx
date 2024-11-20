'use client'

import avatar from '../../../assets/images/avatar.png';
import './friendComponent.scss';
import Image from "next/image";
import { FindChannel } from "@/app/api/channelApi";
import { useTab } from '@/providers/tabProvider';
import { useContactObject } from '@/providers/contactObjectProvider';
import { toast } from 'react-toastify';

export default function FriendComponent(props: any) {

    const { friend, token } = props;

    const { tab } = useTab();
    const { setContactObject } = useContactObject();

    const handleSubscribeChannel = async (friendId: string) => {
        const res = await FindChannel(token, friendId);
        if (res.statusCode === 200) {
            setContactObject(res.data.friend);
        } else {
            toast.error(res.message);
        }
    };

    const handleSubscribeGroup = async (groupId: string) => {

    };

    const formatTime = () => {
        if (friend?.lastMessage?.createAt !== undefined) {
            const dateString = friend?.lastMessage?.createAt;

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
            onClick={() => { tab === 'friends' ? handleSubscribeChannel(friend.userId._id) : handleSubscribeGroup(friend?.groupId) }}>
            <Image src={friend?.avatar || friend?.image_url ? friend?.avatar || friend?.imageUrl : avatar} alt='' />
            <div className='texts'>
                <div className='info'>
                    <div className='title-info'>
                        <span>{friend.userId.name || friend.groupName}</span>
                        <p>{formatTime()}</p>
                    </div>
                    {/* {friend.lastMessage &&
                        <div className='last-message'>
                            <p>{friend.lastMessage.sender}:</p>
                            <p>{friend.lastMessage.content}</p>
                        </div>
                    } */}
                </div>
            </div>
        </div>
    );
};