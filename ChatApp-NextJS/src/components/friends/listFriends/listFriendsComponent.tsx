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
import CreateGroupModal from "@/components/modal/createGroup/createGroupModal";
import WaitingAcceptComponent from "../friend/waitingAccept";

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
                {/* {listContacts && listContacts.length > 0 &&
                    listContacts.map((item: any, index: number) => {
                        const contact = {
                            id: item?.userId?._id || item?.group?._id,
                            name: item?.userId?.name || item?.group?.groupName,
                            avatar: item?.userId?.imageUrl || null,
                            isGroup: tab === 'groups' ? true : false
                        };

                        return (<FriendComponent contact={contact} key={`friend-${index}`}
                            setChatWith={setChatWith} token={token}
                        />)
                    })} */}
                {listContacts && listContacts.length > 0 ?
                    listContacts.map((item: any, index: number) => {
                        let contact = null;
                        if (tab !== 'accepts') {
                            contact = {
                                id: item?.userId?._id || item?.group?._id,
                                name: item?.userId?.name || item?.group?.groupName,
                                avatar: item?.userId?.imageUrl || null,
                                isGroup: tab === 'groups' ? true : false
                            };
                        }

                        return tab === 'accepts' ?
                            (<WaitingAcceptComponent contact={item} key={`friend-${index}`} token={token}
                            />)
                            :
                            (<FriendComponent contact={contact} key={`friend-${index}`}
                                setChatWith={setChatWith} token={token}
                            />)
                    }) : <div className='no-content'>No content</div>
                }
            </div>
            {openModalUserInfo && <AddFriendModal token={token} />}
            {openModalCreateGroup && <CreateGroupModal open={openModalCreateGroup}
                setOpen={setOpenModalCreateGroup} token={token} />}
        </div>
    );
}