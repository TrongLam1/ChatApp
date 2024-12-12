'use client'

import { AcceptFriend, CancelFriend } from '@/app/api/friendshipApi';
import avatar from '@/assets/images/avatar.png';
import { faCircleCheck, faCircleXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

export default function WaitingAcceptComponent(props: any) {
    const { contact, token, setCount, countRequest } = props;

    const [user, setUser] = useState<any>();

    useEffect(() => {
        if (contact?.status === 'pending') setUser(contact?.friendId);
    }, [contact]);

    const handleAcceptFriend = async (friendId: string) => {
        const res = await AcceptFriend(token, { friendId });
        if (res.statusCode === 200) {
            toast.success("Đồng ý kết bạn thành công.");
            setUser(null);
            setCount(countRequest > 0 ? countRequest - 1 : 0);
        } else {
            toast.error(res.message);
        }
    };

    const handleDenyAcceptFriend = async (friendId: string) => {
        const res = await CancelFriend(token, { friendId });
        if (res.statusCode === 200) {
            toast.success("Đã hủy kết bạn.");
            setUser(null);
            setCount(countRequest > 0 ? countRequest - 1 : 0);
        } else {
            toast.error(res.message);
        }
    };

    return (
        <>
            {user &&
                <div className='friend'>
                    <div className='accept-info'>
                        <div className='image-container col-lg-4'>
                            <Image src={user?.imageUrl ? user?.imageUrl : avatar} fill alt='' />
                        </div>
                        <div className='texts col-lg-8'>
                            <span>{user?.name}</span>
                        </div>
                    </div>
                    <div className='action'>
                        <button type='button' className='accept'
                            onClick={() => handleAcceptFriend(user._id)}>
                            <FontAwesomeIcon icon={faCircleCheck} />
                        </button>
                        <button type='button' className='deny'
                            onClick={() => handleDenyAcceptFriend(user._id)}>
                            <FontAwesomeIcon icon={faCircleXmark} />
                        </button>
                    </div>
                </div>
            }
        </>
    );
}