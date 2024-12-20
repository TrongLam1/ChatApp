'use client'

import avatar from '@/assets/images/avatar.png';
import { ChangeAvatar } from "@/app/api/userApi";
import { faCircleXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";
import Image from 'next/image';
import { useRouter } from 'next/navigation';

export default function UpdateAvatarComponent(props: any) {
    const router = useRouter();
    const { update } = useSession();

    const { setIsUpdateAvatar, user, token } = props;

    const [loadingApi, setLoadingApi] = useState<boolean>(false);

    const [file, setFile] = useState<any>();
    const [preview, setPreview] = useState<any>();

    useEffect(() => {
        setPreview(user?.avatar ?? avatar);
    }, [user]);

    const handleChange = (e: any) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);
        setPreview(URL.createObjectURL(selectedFile));
    };

    const handleUpdateAvatar = async () => {
        const formData = new FormData();
        formData.append('file', file);

        setLoadingApi(true);

        const res = await ChangeAvatar(token, formData);
        if (res.statusCode === 200) {
            await update({ avatar: res.data.avatar });
        };

        setLoadingApi(false);
        setIsUpdateAvatar(false);
        router.refresh();
    };

    return (
        <div className='user-info-modal'>
            <div className='header'>
                <h4>Cập nhật ảnh đại diện</h4>
                <FontAwesomeIcon icon={faCircleXmark} onClick={() => setIsUpdateAvatar(false)} />
            </div>
            <div className='user-info-body'>
                <div className='avatar-user-container'>
                    <div className='avatar'>
                        <Image src={preview} fill alt='' />
                    </div>
                    <div className='btn-change-avatar-modal'>
                        <label htmlFor='avatar-preview' >
                            Tải ảnh
                        </label>
                        <input id='avatar-preview' key={preview} type="file"
                            accept="image/*" onChange={handleChange} />
                    </div>
                    {file &&
                        <div className="save-avatar">
                            <button onClick={handleUpdateAvatar} disabled={loadingApi ? true : false}>
                                {loadingApi && <i className="fa-solid fa-sync fa-spin loader"></i>}
                                Lưu
                            </button>
                        </div>}
                </div>
            </div>
        </div>
    );
};