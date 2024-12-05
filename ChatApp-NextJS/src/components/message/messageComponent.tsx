'use client'

import avatar from '@/assets/images/avatar.png';
import { useContactObject } from "@/providers/contactObjectProvider";
import Image from "next/image";

export default function MessageComponent(props: any) {
    const { message, user } = props;

    const { contactObject } = useContactObject();

    const randomColor = () => {
        const color = "#" + Math.floor(Math.random() * 16777215).toString(16);
        return color;
    };

    const convertDate = (isoString: string) => {
        const date = new Date(isoString);

        const day = String(date.getUTCDate()).padStart(2, '0');
        const month = String(date.getUTCMonth() + 1).padStart(2, '0');
        const year = String(date.getUTCFullYear()).slice(-2);
        const hours = String(date.getUTCHours()).padStart(2, '0');
        const minutes = String(date.getUTCMinutes()).padStart(2, '0');

        return `${day}-${month}-${year}, ${hours}:${minutes}`;
    };

    return (
        <div className={`message ${message.sender.name === user.username ? 'own' : 'friend-mess'}`}>
            {user.username !== message.sender.name &&
                <Image src={contactObject?.avatar ? contactObject.avatar : avatar} alt=''
                    width={50} height={50} />}
            <div className='texts'>
                {contactObject.isGroup && user.username !== message.sender.name &&
                    <div className='text-sender'>{message.sender.name}</div>}
                {message.imageUrl && <img src={message.sender.imageUrl} alt='img' />}
                {message.content && <p>{message.content}</p>}
                <span>{convertDate(message.createdAt)}</span>
            </div>
        </div>
    );
};