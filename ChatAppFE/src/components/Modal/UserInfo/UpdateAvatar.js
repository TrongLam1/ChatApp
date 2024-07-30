import { useState } from "react";
import { changeAvatarUser } from '../../../services/UserService';

const UpdateAvatar = (props) => {

    const { setIsUpdateAvatar, avatarUser } = props;

    const [file, setFile] = useState('');
    const [preview, setPreview] = useState(avatarUser);

    const handleChange = (e) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);
        setPreview(URL.createObjectURL(selectedFile));
    };

    const handleUpdateAvatar = async () => {
        const formData = new FormData();
        formData.append('file', file);
        const res = await changeAvatarUser(formData);
        console.log(res);
        if (res && res.status === 200) { setIsUpdateAvatar(false); }
    };

    return (
        <div className='user-info-modal'>
            <div className='header'>
                <h4>Update Avatar</h4>
                <i className="fa-regular fa-circle-xmark" onClick={() => setIsUpdateAvatar(false)}></i>
            </div>
            <div className='user-info-body'>
                <div className='avatar-user-container'>
                    <div className='avatar'>
                        <img src={preview} alt='' />
                    </div>
                    <div className='btn-change-avatar-modal'>
                        <label htmlFor='avatar-preview' >
                            Upload Avatar
                        </label>
                        <input id='avatar-preview' key={preview} type="file"
                            accept="image/*" onChange={handleChange} />
                    </div>
                    {file &&
                        <div className="save-avatar">
                            <button onClick={handleUpdateAvatar}>Save</button>
                        </div>}
                </div>
            </div>
        </div>
    );
};

export default UpdateAvatar;