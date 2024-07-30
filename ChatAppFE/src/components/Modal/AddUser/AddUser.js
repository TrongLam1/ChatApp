import './AddUser.scss';
import UserAddInfo from './UserAddInfo';
import { useEffect, useState } from 'react';
import { findUserByEmail } from '../../../services/UserService';

const AddUser = (props) => {

    const [nameSearch, setNameSearch] = useState('');
    const [user, setUser] = useState('');

    useEffect(() => { }, [user]);

    const handleFindByName = async (e) => {
        e.preventDefault();

        const res = await findUserByEmail(nameSearch);
        if (res && res.status === 200) {
            setUser({ ...res.data });
        } else {
            setUser('');
        }
    };

    return (
        <>
            <div className='add-user-modal'>
                <form onSubmit={(e) => handleFindByName(e)}>
                    <input type='text' placeholder='Username' value={nameSearch}
                        onChange={(e) => setNameSearch(e.target.value)} />
                    <button className='search-btn'>Search</button>
                </form>
                {/* <div className='user'>
                {user !== '' && user.id !== null &&
                    <>
                        <div className='detail'>
                            <img src={avatar} alt='' />
                            <span>{user.userName}</span>
                        </div>
                        {user.status !== 'FRIEND' &&
                            <button className='add-friend-btn'
                                disabled={user.status === "WAITING" ? true : false}>
                                {user.status === "WAITING" ? "Waiting" : "Add user"}
                            </button>
                        }
                    </>
                }
            </div> */}
                <UserAddInfo user={user} />
            </div>
        </>
    )
};

export default AddUser;