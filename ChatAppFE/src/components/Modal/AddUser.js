import './AddUser.scss';
import avatar from '../../assets/image/avatar.jpg';
import { useEffect, useState } from 'react';
import { findUserByEmail } from '../../services/UserService';
import { sendRequestAddFriend } from '../../services/FriendshipService';

const AddUser = (props) => {

    const [nameSearch, setNameSearch] = useState('');
    const [user, setUser] = useState('');

    useEffect(() => {

    }, [user]);

    const handleFindByName = async (e) => {
        e.preventDefault();

        const res = await findUserByEmail(nameSearch);
        if (res && res.status === 200) {
            setUser(res.data);
        } else {
            setUser('');
        }
    };

    const handleSendRequestAddFriend = async () => {
        const res = await sendRequestAddFriend(user.id);
        if (res && res.status === 200) {
            setUser(res.data);
        } else {
            setUser('');
        }
    };

    return (
        <div className='add-user-modal'>
            <form onSubmit={(e) => handleFindByName(e)}>
                <input type='text' placeholder='Username' value={nameSearch}
                    onChange={(e) => setNameSearch(e.target.value)} />
                <button className='search-btn'>Search</button>
            </form>
            <div className='user'>
                {user !== '' && user.id !== null &&
                    <>
                        <div className='detail'>
                            <img src={avatar} alt='' />
                            <span>{user.userName}</span>
                        </div>
                        {user.status !== 'FRIEND' &&
                            <button className='add-friend-btn'
                                disabled={user.status === "WAITING" ? true : false}
                                onClick={handleSendRequestAddFriend}>
                                {user.status === "WAITING" ? "Waiting" : "Add user"}
                            </button>
                        }
                    </>
                }
            </div>
        </div>
    )
};

export default AddUser;