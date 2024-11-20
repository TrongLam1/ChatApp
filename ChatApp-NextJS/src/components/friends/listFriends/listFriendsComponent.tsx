'use client'

import { useState } from "react";
import './listFriendsComponent.scss';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faMinus, faPlus, faUserGroup } from "@fortawesome/free-solid-svg-icons";
import UserInfoComponent from "@/components/userInfo/userInfoComponent";
import { useTab } from "@/providers/tabProvider";
import FriendComponent from "../friend/friendComponent";
import AddFriendModal from "@/components/modal/addFriend/addFriendModal";
import { useRouter } from "next/navigation";

export default function ListFriendsComponent(props: any) {
    const router = useRouter();

    const { user, token, listContacts } = props;

    const { tab, setTab } = useTab();

    const [username, setUsername] = useState('');
    const [openModalUserInfo, setOpenModalUserInfo] = useState(false);
    const [openModalCreateGroup, setOpenModalCreateGroup] = useState(false);
    const [chatWith, setChatWith] = useState();
    const [amountAddFriend, setAmountAddFriend] = useState(0);

    const handleFindFriendByUsername = async () => { }

    const handleNavigateTab = (tab: string) => {
        setTab(tab);
        router.push(`?tab=${tab}`);
    };

    return (
        <div className='list-friends-container'>
            <UserInfoComponent user={user} />
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
                    (<button type='button' className='add'
                        onClick={() =>
                            setOpenModalUserInfo((prev) => !prev)
                        }>
                        {openModalUserInfo === false ?
                            <FontAwesomeIcon icon={faPlus} /> :
                            <FontAwesomeIcon icon={faMinus} />}
                    </button>)
                    :
                    (<button type='button' className='new-group'
                        onClick={() => setOpenModalCreateGroup(!openModalCreateGroup)}>
                        <FontAwesomeIcon icon={faUserGroup} />
                    </button>)
                }
            </div>
            <div className='tab'>
                <div>
                    <button className={`${tab === 'friends' ? 'active' : ''}`}
                        onClick={() => handleNavigateTab('friends')}>Friends</button>
                </div>
                <div>
                    <button className={`${tab === 'groups' ? 'active' : ''}`}
                        onClick={() => handleNavigateTab('groups')}>Groups</button>
                </div>
                <div className='amount-add-friend'>
                    <button className={`${tab === 'accepts' ? 'active' : ''}`}
                        onClick={() => handleNavigateTab('accepts')}>Accepts</button>
                    <div className='amount'>{amountAddFriend}</div>
                </div>
            </div>
            <div className='list-chats'>
                {listContacts && listContacts.length > 0 &&
                    listContacts.map((item: any, index: number) => {
                        return (<FriendComponent friend={item} key={`friend-${index}`}
                            setChatWith={setChatWith} token={token}
                        />)
                    })}
                {/* {listChats && listChats.length > 0 ?
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
                } */}
            </div>
            {openModalUserInfo && <AddFriendModal token={token} />}
            {/* {openModalCreateGroup && <CreateGroup open={openModalCreateGroup}
                setOpen={setOpenModalCreateGroup} />} */}
        </div>
    );
}