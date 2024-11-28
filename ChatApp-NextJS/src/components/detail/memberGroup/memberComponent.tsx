'use client'

import { useState } from 'react';
import './memberComponent.scss';
import avatar from '@/assets/images/avatar.png';
import Image from 'next/image';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleXmark, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import ConfirmModal from '@/components/modal/confirmModal/confirmModal';

export default function MemberComponent(props: any) {
    const { member } = props;

    const [user, setUser] = useState(member.friendId);
    const [status, setStatus] = useState(member.status);

    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [message, setMessage] = useState<any>('');

    const closeModal = () => {
        setIsOpen(false);
    };

    const confirmModal = () => {
        setMessage(<>Are you sure remove <strong>&quot;{user.name}&quot;</strong> from group?</>);
        setIsOpen(true);
    };

    const handleRequestFriend = async (userId: string) => {

    };

    const handleRemoveMember = async () => {

    };

    return (
        <>
            <div className='member-group'>
                <div className='info'>
                    <Image src={user?.avatar ? user?.avatar : avatar} alt='' />
                    <span>{user?.name}</span>
                </div>
                <div className='action'>
                    <FontAwesomeIcon icon={faCircleXmark} className='remove-member'
                        onClick={confirmModal} />
                    {status === null &&
                        <FontAwesomeIcon icon={faUserPlus} className='request-friend'
                            onClick={() => handleRequestFriend(user._id)} />}
                </div>
            </div>
            <ConfirmModal isOpen={isOpen} closeModal={closeModal} message={message}
                onConfirm={handleRemoveMember} />
        </>
    );
}