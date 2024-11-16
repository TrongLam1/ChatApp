'use client'

import { useState } from "react";
import './listFriendsComponent.scss';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import UserInfoComponent from "@/components/userInfo/userInfoComponent";

export default function ListFriendsComponent(props: any) {

    const [username, setUsername] = useState('');
    const [openModalUserInfo, setOpenModalUserInfo] = useState(false);
    const [openModalCreateGroup, setOpenModalCreateGroup] = useState(false);
    const [listChats, setListChats] = useState([]);
    const [tab, setTab] = useState('');
    const [amountAddFriend, setAmountAddFriend] = useState(0);

    const handleFindFriendByUsername = async () => { }

    return (
        <div className='list-friends-container'>
            {/* user={user} */}
            <UserInfoComponent />
            <div className='search'>
                <div className='search-bar'>
                    <FontAwesomeIcon icon={faMagnifyingGlass} onClick={handleFindFriendByUsername} />
                    <input type='text' placeholder='Enter username...'
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        onKeyDown={handleFindFriendByUsername}
                    />
                </div>
                {tab !== 'groups' ?
                    (<button type='button' className='add' onClick={() => setOpenModalUserInfo((prev) => !prev)}>
                        {openModalUserInfo === false ?
                            <FontAwesomeIcon icon={faPlus} /> :
                            <FontAwesomeIcon icon={faMinus} />}
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
            {/* <div className='list-chats'>
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
                                handleShowContentForTab={handleShowContentForTab}
                            />)
                    }) : <div className='no-content'>No content</div>
                }
            </div> */}
            {/* {openModalUserInfo && <AddUser />}
            {openModalCreateGroup && <CreateGroup open={openModalCreateGroup}
                setOpen={setOpenModalCreateGroup} />} */}
        </div>
    );
}