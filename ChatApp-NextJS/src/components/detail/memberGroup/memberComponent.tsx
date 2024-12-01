'use client'

import { RemoveMember } from '@/app/api/groupApi';
import avatar from '@/assets/images/avatar.png';
import ConfirmModal from '@/components/modal/confirmModal/confirmModal';
import { useContactObject } from '@/providers/contactObjectProvider';
import { faCircleXmark, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import { useState } from 'react';
import './memberComponent.scss';
import { toast } from 'react-toastify';

export default function MemberComponent(props: any) {
    const { member, token, updateInfoGroup, setMembersOfGroup } = props;
    const { contactObject, setContactObject } = useContactObject();

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
        const res = await RemoveMember(token, {
            groupId: contactObject.id, memberId: user._id
        });
        if (res.statusCode === 200) {
            updateInfoGroup();
            setContactObject({
                ...contactObject, members: contactObject.members - 1
            });
            setMembersOfGroup((prevMembers: any[]) => {
                prevMembers.filter((member) => member._id !== user._id)
            });
        } else {
            toast.error(res.message);
            closeModal();
        }
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