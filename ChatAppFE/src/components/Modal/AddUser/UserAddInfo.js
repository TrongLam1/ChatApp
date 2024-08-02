import { useEffect, useState } from 'react';
import avatar from '../../../assets/image/avatar.jpg';
import { sendRequestAddFriend, cancelAcceptFriend } from '../../../services/FriendshipService';

const UserAddInfo = (props) => {

    const { user } = props;

    const [userInfo, setUserInfo] = useState(user);

    useEffect(() => {
        setUserInfo(user);
    }, [user]);

    const handleSendRequestAddFriend = async () => {
        const res = await sendRequestAddFriend(user.id);
        console.log(res);
        if (res && res.status === 200) {
            setUserInfo(res.data);
        } else {
            setUserInfo('');
        }
    };

    const handleCancelAddFriend = async () => {
        const res = await cancelAcceptFriend(user.id);
        if (res && res.status === 200) {
            setUserInfo(res.data);
        } else {
            setUserInfo('');
        }
    };

    return (
        <div className='user'>
            {userInfo !== '' && userInfo?.id !== null ?
                <>
                    <div className='detail'>
                        <img src={avatar} alt='' />
                        <span>{userInfo?.userName}</span>
                    </div>
                    {<>
                        {userInfo?.status === "WAITING" &&
                            <button className='cancel-friend-btn' onClick={handleCancelAddFriend}>
                                Cancel
                            </button>}
                        {!userInfo?.status &&
                            <button className='add-friend-btn' onClick={handleSendRequestAddFriend}>
                                Add friend
                            </button>}
                    </>}
                </> : (<div className='no-content'>Not found user</div>)
            }
        </div>
    );
};

export default UserAddInfo;