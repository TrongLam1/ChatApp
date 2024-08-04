import './ListFriends.scss';
import avatar from '../../assets/image/avatar.jpg';
import logo from '../../assets/image/logo.png';
import { UserContext } from '../../context/UserContext';
import Friend from './Friend';
import WaitingAcceptFriend from './WaitingAcceptFriend';
import AddUser from '../Modal/AddUser/AddUser';
import UserInfo from './UserInfo/UserInfo';
import CreateGroup from '../Modal/CreateGroup/CreateGroup';
import { toast } from 'react-toastify';
import { useEffect, useState, useContext } from 'react';
import {
    fetchListFriends, fetchListFriendsWaitingAccept,
    countRequestsAddFriend, findFriendByUsername
} from '../../services/FriendshipService';
import { fetchGroupsFromUser } from '../../services/GroupService';
import { WebSocketContext } from '../../context/WebSocketContext';

const Friends = (props) => {

    const { setChatWith, tab, setTab } = props;

    const { user } = useContext(UserContext);
    const { notifyAddFriend, setNotifyAddFriend } = useContext(WebSocketContext);

    const [username, setUsername] = useState('');
    const [openModalUserInfo, setOpenModalUserInfo] = useState(false);
    const [openModalCreateGroup, setOpenModalCreateGroup] = useState(false);
    const [listChats, setListChats] = useState([]);
    const [amountAddFriend, setAmountAddFriend] = useState(0);

    useEffect(() => {
        handleShowContentForTab();
        fetchAmountRequestsAddFriend();
    }, [tab]);

    useEffect(() => {
        if (notifyAddFriend !== '') {
            toast.success(notifyAddFriend);
            fetchAmountRequestsAddFriend();
        }

        return (() => setNotifyAddFriend(''));
    }, [notifyAddFriend]);

    const getListChats = async () => {
        const res = await fetchListFriends();
        if (res && res.status === 200) {
            setListChats(res.data);
        }
    };

    const getListChatsWaitingAccept = async () => {
        const res = await fetchListFriendsWaitingAccept();
        if (res && res.status === 200) {
            setListChats(res.data);
        }
    };

    const getListGroupsFromUser = async () => {
        const res = await fetchGroupsFromUser();
        if (res && res.status === 200) {
            setListChats(res.data);
        }
    };

    const fetchAmountRequestsAddFriend = async () => {
        const res = await countRequestsAddFriend();
        if (res && res.status === 200) {
            setAmountAddFriend(res.data);
        }
    };

    const handleFindFriendByUsername = async (e) => {
        if (e.key === 'Enter') {
            if (username !== '') {
                const res = await findFriendByUsername(username);
                if (res && res.status === 200) setListChats(res.data);
            } else {
                getListChats();
            }
        };
    };

    const handleShowContentForTab = async () => {
        switch (tab) {
            case 'friends':
                getListChats();
                break;
            case 'groups':
                getListGroupsFromUser();
                break;
            case 'accepts':
                getListChatsWaitingAccept();
                break;
            default:
                setListChats([]);
        }
    };

    return (
        <>
            <div className='list-friends-container'>
                <UserInfo user={user} />
                <div className='search'>
                    <div className='search-bar'>
                        <i className="fa-solid fa-magnifying-glass" onClick={handleFindFriendByUsername} />
                        <input type='text' placeholder='Enter username...'
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            onKeyDown={handleFindFriendByUsername}
                        />
                    </div>
                    {tab !== 'groups' ?
                        (<button type='button' className='add' onClick={() => setOpenModalUserInfo((prev) => !prev)}>
                            {openModalUserInfo === false ?
                                <i className="fa-solid fa-plus"></i> :
                                <i className="fa-solid fa-minus"></i>}
                        </button>)
                        :
                        (<button type='button' className='new-group'
                            onClick={() => setOpenModalCreateGroup(!openModalCreateGroup)}>
                            <i className="fa-solid fa-user-group"></i>
                        </button>)
                    }
                </div>
                <div className='tab'>
                    <div>
                        <button className={`${tab === 'friends' ? 'active' : ''}`}
                            onClick={() => setTab('friends')}>Friends</button>
                    </div>
                    <div>
                        <button className={`${tab === 'groups' ? 'active' : ''}`}
                            onClick={() => setTab('groups')}>Groups</button>
                    </div>
                    <div className='amount-add-friend'>
                        <button className={`${tab === 'accepts' ? 'active' : ''}`}
                            onClick={() => setTab('accepts')}>Accepts</button>
                        <div className='amount'>{amountAddFriend}</div>
                    </div>
                </div>
                <div className='list-chats'>
                    {listChats && listChats.length > 0 ?
                        listChats.map((item, index) => {
                            return tab === 'accepts' ?
                                (<WaitingAcceptFriend item={item} key={`friend-${index}`}
                                    getListChatsWaitingAccept={getListChatsWaitingAccept}
                                    refreshAmountRequest={fetchAmountRequestsAddFriend}
                                />)
                                :
                                (<Friend item={item} key={`friend-${index}`} tab={tab}
                                    setChatWith={setChatWith}
                                />)
                        }) : <div className='no-content'>No content</div>
                    }
                </div>
                {openModalUserInfo && <AddUser />}
                {openModalCreateGroup && <CreateGroup open={openModalCreateGroup}
                    setOpen={setOpenModalCreateGroup} />}
            </div>
        </>
    );
};

export default Friends;