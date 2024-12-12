'use client'

import { FindChannel } from "@/app/api/channelApi";
import { FindGroupById } from "@/app/api/groupApi";
import group from '@/assets/images/group.png';
import { useContactObject } from '@/providers/contactObjectProvider';
import { useTab } from '@/providers/tabProvider';
import Image from "next/image";
import { toast } from 'react-toastify';
import avatar from '@/assets/images/avatar.png';
import './friendComponent.scss';

export default function FriendComponent(props: any) {

    const { contact, token } = props;

    const { tab } = useTab();
    const { setContactObject } = useContactObject();

    const handleSubscribeChannel = async () => {
        const res = await FindChannel(token, contact.id);
        if (res.statusCode === 200) {
            setContactObject({
                id: contact.id,
                name: contact.name,
                channelId: res.data.channelId,
                avatar: contact.avatar
            });
        } else { toast.error(res.message); }
    };

    const handleSubscribeGroup = async () => {
        const res = await FindGroupById(token, contact.id);
        if (res.statusCode === 200) {
            setContactObject({
                id: res.data.group.id,
                name: res.data.group.groupName,
                isGroup: true,
                members: res.data.members
            });
        } else {
            toast.error(res.message);
        }
    };

    const handleSubscribe = async () => {
        if (tab === 'groups') {
            handleSubscribeGroup();
        } else {
            handleSubscribeChannel();
        }
    }

    const formatTime = () => {
        if (contact?.lastMessage?.createAt !== undefined) {
            const dateString = contact?.lastMessage?.createAt;

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
        <div className='friend' onClick={handleSubscribe}>
            {contact.isGroup ?
                <div className="image-container col-4">
                    <Image src={group} alt='' fill />
                </div> :
                <div className="image-container col-8">
                    <Image src={contact.avatar !== null ? contact.avatar : avatar} alt=''
                        fill />
                </div>}
            <div className='texts'>
                <div className='info'>
                    <div className='title-info'>
                        <span>{contact?.name}</span>
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