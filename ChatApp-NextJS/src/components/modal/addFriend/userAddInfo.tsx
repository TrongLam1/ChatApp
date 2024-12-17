'use client'

import { AcceptFriend, CancelFriend, RequestFriend } from '@/app/api/friendshipApi';
import avatar from '@/assets/images/avatar.png';
import Image from 'next/image';
import { useEffect, useState } from 'react';

export default function UserAddInfo(props: any) {

    const { user, token } = props;

    const [userInfo, setUserInfo] = useState<any>();
    const [status, setStatus] = useState<any>();

    useEffect(() => {
        setUserInfo(user.friendId);
        setStatus(user.status);
    }, [user]);

    const handleSendRequestAddFriend = async () => {
        const res = await RequestFriend(token, { friendId: user.friendId._id });
        if (res.statusCode === 201) {
            setStatus(res.data.status);
        };
    };

    const handleAcceptFriend = async () => {
        const res = await AcceptFriend(token, { friendId: user.friendId._id });
        if (res.statusCode === 200) {
            setStatus(res.data.status);
        }
    };

    const handleCancelAddFriend = async () => {
        const res = await CancelFriend(token, { friendId: user.friendId._id });
        if (res.statusCode === 200) {
            setStatus(null);
        }
    };

    return (
        <div className='user'>
            {userInfo &&
                <>
                    <div className='detail'>
                        <div className='avatar'>
                            <Image src={userInfo?.imageUrl ? userInfo?.imageUrl : avatar} fill alt='' />
                        </div>
                        <span>{userInfo?.name}</span>
                    </div>
                    {<>
                        {status === "waiting" &&
                            <button className='cancel-friend-btn' onClick={handleCancelAddFriend}>
                                Hủy
                            </button>}
                        {status === "pending" &&
                            <button className='accept-friend-btn' onClick={handleAcceptFriend}>
                                Đồng ý
                            </button>}
                        {!status &&
                            <button className='add-friend-btn' onClick={handleSendRequestAddFriend}>
                                Thêm
                            </button>}
                    </>}
                </>
            }
        </div>
    );
};