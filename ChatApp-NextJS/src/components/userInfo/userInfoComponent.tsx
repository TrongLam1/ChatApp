'use client'

import { faEllipsis } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { signOut } from 'next-auth/react';
import { useState } from 'react';
import { Dropdown } from 'react-bootstrap';
import avatar from '../../assets/images/avatar.png';
import ProfileComponent from './profile/profileComponent';
import './userInfoComponent.scss';
import Image from 'next/image';

export default function UserInfoComponent(props: any) {

    const { user, token } = props;

    const [open, setOpen] = useState(false);

    return (
        <>
            <div className='user-info-container'>
                <div className='user'>
                    <div className='avatar'>
                        <Image src={user?.avatar ? user?.avatar : avatar} fill alt='avatar' />
                    </div>
                    <h5>{user?.username}</h5>
                </div>
                <div className='icons'>
                    <Dropdown>
                        <Dropdown.Toggle className='setting'>
                            <FontAwesomeIcon icon={faEllipsis} />
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item onClick={() => setOpen(!open)}>Thông tin cá nhân</Dropdown.Item>
                            <Dropdown.Item onClick={() => signOut({ callbackUrl: '/login' })}>
                                Đăng xuất
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
            </div>
            {open && <ProfileComponent token={token} user={user} setOpen={setOpen} />}
        </>
    )
}