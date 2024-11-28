'use client'

import { faEllipsis } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import { useState } from 'react';
import { Dropdown } from 'react-bootstrap';
import avatar from '../../assets/images/avatar.png';
import './userInfoComponent.scss';
import { signOut } from 'next-auth/react';

export default function UserInfoComponent(props: any) {

    const { user } = props;

    const [open, setOpen] = useState(false);

    return (
        <>
            <div className='user-info-container'>
                <div className='user'>
                    <Image src={user?.avatar ? user?.avatar : avatar} alt='' />
                    <h5>{user?.username}</h5>
                </div>
                <div className='icons'>
                    <Dropdown>
                        <Dropdown.Toggle className='setting'>
                            <FontAwesomeIcon icon={faEllipsis} />
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item onClick={() => setOpen(!open)}>Profile</Dropdown.Item>
                            <Dropdown.Item onClick={() => signOut({ callbackUrl: '/login' })}>
                                Logout
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
            </div>
            {/* <UserProfile username={user.username} open={open} setOpen={setOpen} /> */}
        </>
    )
}