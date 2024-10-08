import './UserProfile.scss';
import avatar from '../../../assets/image/avatar.jpg';
import { toast } from 'react-toastify';
import { useContext, useState } from 'react';
import { UserContext } from '../../../context/UserContext';
import { updateUsername } from '../../../services/UserService';
import UpdateAvatar from './UpdateAvatar';

const UserProfile = (props) => {

    const { open, setOpen, username } = props;

    const { user, setUser } = useContext(UserContext);

    const [editUsername, setEditUsername] = useState(username);
    const [isEditUsername, setIsEditUsername] = useState(false);

    const [isUpdateAvatar, setIsUpdateAvatar] = useState(false);

    const handleUpdateUsername = async (e) => {
        e.preventDefault();
        const res = username !== editUsername && await updateUsername(editUsername);
        if (res && res.status === 200) {
            toast.success("Successfully updated username");
            setUser(prevState => ({
                ...prevState,
                username: res.data.userName
            }));
            setOpen(false);
        } else {
            toast.error(res.message);
        }
    };

    return (
        <>
            {open && (
                !isUpdateAvatar ? (
                    <div className='user-info-modal'>
                        <div className='header'>
                            <h4>User's Profile</h4>
                            <i className="fa-regular fa-circle-xmark" onClick={() => setOpen(false)}></i>
                        </div>
                        <div className='user-info-body'>
                            <div className='avatar-user-container'>
                                <div className='avatar'>
                                    <img src={user.avatar ? user.avatar : avatar} alt='' />
                                </div>
                                <div className='btn-change-avatar'>
                                    <label onClick={() => setIsUpdateAvatar(!isUpdateAvatar)} >
                                        <i className="fa-solid fa-camera"></i>
                                    </label>
                                </div>
                            </div>
                            <div className='username-container'>
                                <input className='username' type='text'
                                    value={isEditUsername ? editUsername : username}
                                    onChange={(e) => setEditUsername(e.target.value)}
                                    disabled={isEditUsername ? false : true}
                                />
                                <div className='btn-change-username'>
                                    <button onClick={() => setIsEditUsername(!isEditUsername)}>
                                        {isEditUsername ?
                                            <i className="fa-solid fa-download"
                                                onClick={handleUpdateUsername}></i>
                                            :
                                            <i className="fa-solid fa-pencil"></i>
                                        }
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <UpdateAvatar
                        setIsUpdateAvatar={setIsUpdateAvatar}
                        avatarUser={user.avatar} />
                )
            )}
        </>
    );
};

export default UserProfile;