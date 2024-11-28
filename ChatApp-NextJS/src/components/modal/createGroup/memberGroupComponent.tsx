'use client'

import Image from 'next/image';
import './memberGroupComponent.scss';
import avatar from '@/assets/images/avatar.png';
import { useState } from 'react';

export default function MemberGroupComponent(props: any) {
    const { member, setListAddGroup } = props;

    const [user, setUser] = useState<any>(member.userId);

    const handleAddFriendToGroup = (e: any) => {
        const isChecked = e.target.checked;

        if (isChecked) {
            setListAddGroup((prevList: any) => [...new Set([...prevList, user._id])]);
        } else {
            setListAddGroup((prevList: any) =>
                prevList.filter((id: string) => id !== member.userId._id));
        }
    };

    return (
        <div className="friend-group-item">
            <input type="checkbox" id={user?._id} onClick={(e) => handleAddFriendToGroup(e)} />
            <label htmlFor={user?._id}>
                <Image src={user?.avatar ? user?.avatar : avatar} alt='' />
                <div className='texts'>
                    <div className='info'>
                        <p>{user?.name}</p>
                    </div>
                </div>
            </label>
        </div>
    );
}