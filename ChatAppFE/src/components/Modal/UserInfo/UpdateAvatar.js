import { useContext, useState } from "react";
import { changeAvatarUser } from '../../../services/UserService';
import { UserContext } from "../../../context/UserContext";

const UpdateAvatar = (props) => {

    const { setIsUpdateAvatar, avatarUser } = props;

    const { setUser } = useContext(UserContext);

    const [loadingApi, setLoadingApi] = useState(false);

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

        setLoadingApi(true);

        const res = await changeAvatarUser(formData);
        if (res && res.status === 200) {
            setUser(prevState => ({
                ...prevState,
                avatar: res.data.image_url
            }))
            setIsUpdateAvatar(false);
        }

        setLoadingApi(false);
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
                            <button onClick={handleUpdateAvatar} disabled={loadingApi ? true : false}>
                                {loadingApi && <i className="fa-solid fa-sync fa-spin loader"></i>}
                                Save
                            </button>
                        </div>}
                </div>
            </div>
        </div>
    );
};

export default UpdateAvatar;