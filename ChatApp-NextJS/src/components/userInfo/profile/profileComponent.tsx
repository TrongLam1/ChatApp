'use client'

import { UpdateProfile } from '@/app/api/userApi';
import avatar from '@/assets/images/avatar.png';
import { faCamera, faCircleXmark, faDownload, faPencil } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useSession } from 'next-auth/react';
import { useRouter } from 'next/navigation';
import { useState } from "react";
import { toast } from 'react-toastify';
import './profileComponent.scss';
import UpdateAvatarComponent from './updateAvatarComponent';
import Image from 'next/image';

export default function ProfileComponent(props: any) {
    const router = useRouter();

    const { update } = useSession();

    const { setOpen, user, token } = props;

    const [editUsername, setEditUsername] = useState<string>(user.username);
    const [isEditUsername, setIsEditUsername] = useState<boolean>(false);

    const [isUpdateAvatar, setIsUpdateAvatar] = useState<boolean>(false);

    const handleUpdateUsername = async () => {
        const res = await UpdateProfile(token, { username: editUsername, phone: null });
        if (res.statusCode === 200) {
            await update({ username: res.data.username });
            toast.success("Cập nhật thành công.");
            router.refresh();
        };
    };

    return (
        <>
            {
                !isUpdateAvatar ? (
                    <div className='user-info-modal'>
                        <div className='header'>
                            <h4>Thông tin cá nhân</h4>
                            <FontAwesomeIcon icon={faCircleXmark} onClick={() => setOpen(false)} />
                        </div>
                        <div className='user-info-body'>
                            <div className='avatar-user-container'>
                                <div className='avatar'>
                                    <Image src={user?.avatar ?? avatar} fill alt='' />
                                </div>
                                <div className='btn-change-avatar'>
                                    <label onClick={() => setIsUpdateAvatar(!isUpdateAvatar)} >
                                        <FontAwesomeIcon icon={faCamera} />
                                    </label>
                                </div>
                            </div>
                            <div className='username-container'>
                                <input placeholder="Enter name" className='username' type='text'
                                    value={isEditUsername ? editUsername : user.username}
                                    onChange={(e) => setEditUsername(e.target.value)}
                                    disabled={isEditUsername ? false : true}
                                />
                                <div className='btn-change-username'>
                                    <button onClick={() => setIsEditUsername(!isEditUsername)}>
                                        {isEditUsername ?
                                            <FontAwesomeIcon icon={faDownload}
                                                onClick={handleUpdateUsername} />
                                            :
                                            <FontAwesomeIcon icon={faPencil} />
                                        }
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <UpdateAvatarComponent
                        setIsUpdateAvatar={setIsUpdateAvatar}
                        user={user} token={token} />
                )
            }
        </>
    );
};