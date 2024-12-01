'use client'

import { FindChannel } from "@/app/api/channelApi";
import { useContactObject } from '@/providers/contactObjectProvider';
import { useTab } from '@/providers/tabProvider';
import Image from "next/image";
import { toast } from 'react-toastify';
import avatar from '../../../assets/images/avatar.png';
import group from '@/assets/images/group.png';
import './friendComponent.scss';
import { FindGroupById } from "@/app/api/groupApi";

export default function FriendComponent(props: any) {

    const { contact, token } = props;

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
        const res = await FindGroupById(token, groupId);
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
        <div className='friend'
            onClick={() => { tab === 'friends' ? handleSubscribeChannel(contact.id) : handleSubscribeGroup(contact.id) }}>
            {contact.isGroup ?
                <Image src={group} alt='' width={50} height={50} /> :
                <Image src={contact?.avatar ? contact?.avatar : avatar} alt=''
                    width={50} height={50} />}
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