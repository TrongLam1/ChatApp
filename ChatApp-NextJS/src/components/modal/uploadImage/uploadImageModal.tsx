'use client'

import defaultUpload from '@/assets/images/defaultUpload.jpg';
import { useState } from 'react';
import './uploadImageModal.scss';
import { useTab } from '@/providers/tabProvider';
import { ChannelSendImage, GroupSendImage } from '@/app/api/messageApi';
import Image from 'next/image';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleXmark } from '@fortawesome/free-solid-svg-icons';
import { useContactObject } from '@/providers/contactObjectProvider';

export default function UploadImageModal(props: any) {
    const { setOpen, token } = props;

    const { tab } = useTab();
    const { contactObject } = useContactObject();

    const [file, setFile] = useState<any>(null);
    const [preview, setPreview] = useState<any>(defaultUpload);
    const [hasImg, setHasImg] = useState(false);

    const handleChange = (e) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);
        setPreview(URL.createObjectURL(selectedFile));
        setHasImg(true);
    };

    const handleRemoveImg = () => {
        setFile(null);
        setPreview(null);
        setHasImg(false);
        setOpen(false);
    };

    const handleSendImg = async () => {
        const formData = new FormData();
        const id = contactObject.isGroup ? contactObject.id : contactObject.channelId;
        formData.append('id', id);
        formData.append('file', file);

        const res = tab === 'friends' ? await ChannelSendImage(token, formData)
            : await GroupSendImage(token, formData);

        if (res.statusCode === 201) {
            handleRemoveImg();
            setOpen(false);
        }
    };

    return (
        <div className='modal-upload-img'>
            <div className='img-preview'>
                <div className='close-modal'>
                    <FontAwesomeIcon icon={faCircleXmark} onClick={handleRemoveImg} />
                </div>
                <div className='image-container'>
                    <Image src={preview} fill alt='' />
                </div>
            </div>
            <div className='btn'>
                <div className='btn-upload-img'>
                    <label htmlFor='img-preview' >Upload Image</label>
                    <input id='img-preview' key={preview} type="file"
                        accept="image/*" onChange={handleChange} />
                </div>
                {hasImg &&
                    <div className='btn-send-img' onClick={handleSendImg}>
                        <p>Send Img</p>
                    </div>
                }
            </div>
        </div>
    );
}