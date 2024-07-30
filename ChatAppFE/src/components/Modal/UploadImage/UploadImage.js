import { useEffect, useState } from 'react';
import './UploadImage.scss';
import { sendImageToChannel } from '../../../services/ChannelService';
import { sendImageToGroup } from '../../../services/GroupService';

const UploadImage = (props) => {

    const { open, setOpen, subscribe, handleGetMessage, tab } = props;

    const [file, setFile] = useState(null);
    const [preview, setPreview] = useState('');
    const [hasImg, setHasImg] = useState(false);

    const handleChange = (e) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);
        setPreview(URL.createObjectURL(selectedFile));
        setHasImg(true);
    };

    const handleRemoveImg = () => {
        setFile(null);
        setPreview('');
        setHasImg(false);
    };

    const handleSendImg = async () => {
        const formData = new FormData();
        formData.append('subscribe', subscribe);
        formData.append('content', '');
        formData.append('image_url', '');
        formData.append('image_id', '');
        formData.append('file', file);
        const res = tab === 'friends' ? await sendImageToChannel(formData)
            : await sendImageToGroup(formData);
        if (res.status === 200) {
            handleRemoveImg();
            setOpen(false);
            handleGetMessage();
        }
    };

    return (
        <>
            {open &&
                <div className='modal-upload-img'>
                    <div className='img-preview'>
                        {hasImg && <i className="fa-regular fa-circle-xmark" onClick={handleRemoveImg}></i>}
                        <img src={preview} alt='' />
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
            }
        </>
    );
};

export default UploadImage;