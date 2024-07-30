import './ListFriends.scss';
import avatar from '../../assets/image/avatar.jpg';
import logo from '../../assets/image/logo.png';
import { UserContext } from '../../context/UserContext';
import Friend from './Friend';
import WaitingAcceptFriend from './WaitingAcceptFriend';
import AddUser from '../Modal/AddUser/AddUser';
import UserInfo from './UserInfo/UserInfo';
import CreateGroup from '../Modal/CreateGroup/CreateGroup';
import { useEffect, useState, useContext } from 'react';
import { fetchListFriends, fetchListFriendsWaitingAccept } from '../../services/FriendshipService';
import { fetchGroupsFromUser, createNewGroup } from '../../services/GroupService';

const Friends = (props) => {

    const { setChatWith, tab, setTab } = props;

    const { user } = useContext(UserContext);
    const [openModalUserInfo, setOpenModalUserInfo] = useState(false);
    const [openModalCreateGroup, setOpenModalCreateGroup] = useState(false);
    const [listChats, setListChats] = useState([]);

    useEffect(() => {
        handleShowContentForTab();
    }, [tab]);

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
                        <i className="fa-solid fa-magnifying-glass"></i>
                        <input type='text' placeholder='Enter...' />
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
                    <div>
                        <button className={`${tab === 'accepts' ? 'active' : ''}`}
                            onClick={() => setTab('accepts')}>Accepts</button>
                    </div>
                </div>
                <div className='list-chats'>
                    {listChats && listChats.length > 0 ?
                        listChats.map((item, index) => {
                            return tab === 'accepts' ?
                                (<WaitingAcceptFriend item={item} key={`friend-${index}`}
                                    getListChatsWaitingAccept={getListChatsWaitingAccept}
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