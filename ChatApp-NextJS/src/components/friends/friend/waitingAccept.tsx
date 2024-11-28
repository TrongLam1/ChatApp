'use client'

import { AcceptFriend } from '@/app/api/friendshipApi';
import avatar from '@/assets/images/avatar.png';
import { faCircleCheck, faCircleXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import { useState } from 'react';
import { toast } from 'react-toastify';

export default function WaitingAcceptComponent(props: any) {
    const { contact, token } = props;

    const [user, setUser] = useState<any>(contact.friendId);

    const handleAcceptFriend = async (friendId: string) => {
        const res = await AcceptFriend(token, { friendId });
        if (res.statusCode === 200) {
            toast.success("Add friend successfully.");
            setUser(null);
        } else {
            toast.error(res.message);
        }
    };

    const handleDenyAcceptFriend = async () => {

    };

    return (
        <>
            {user &&
                <div className='friend'>
                    <div className='accept-info'>
                        <Image src={user?.avatar ? user?.avatar : avatar} alt='' />
                        <div className='texts'>
                            <span>{user?.name}</span>
                        </div>
                    </div>
                    <div className='action'>
                        <button type='button' className='accept'
                            onClick={() => handleAcceptFriend(user._id)}>
                            <FontAwesomeIcon icon={faCircleCheck} />
                        </button>
                        <button type='button' className='deny'
                            onClick={() => handleDenyAcceptFriend()}>
                            <FontAwesomeIcon icon={faCircleXmark} />
                        </button>
                    </div>
                </div>
            }
        </>
    );
}