'use client'

import { useEffect, useState } from "react";
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
import { FindUsersByName } from "@/app/api/friendshipApi";

export default function ListFriendsComponent(props: any) {
    const router = useRouter();

    const { user, token, listContacts, countRequest, setCount } = props;

    const { tab, setTab } = useTab();

    const [username, setUsername] = useState('');
    const [openModalUserInfo, setOpenModalUserInfo] = useState(false);
    const [openModalCreateGroup, setOpenModalCreateGroup] = useState(false);
    const [listFriends, setListFriends] = useState(listContacts);

    useEffect(() => {
        setListFriends(listContacts);
    }, [listContacts]);

    const handleFindFriendByUsername = async () => {
        if (username !== '') {
            const res = await FindUsersByName(token, username);
            if (res.statusCode === 200) {
                setListFriends(res.data);
            }
        } else {
            setListFriends(listContacts);
        }
    };

    const handleNavigateTab = (tab: string) => {
        setTab(tab);
        router.push(`?tab=${tab}`);
    };

    return (
        <div className='list-friends-container'>
            <UserInfoComponent token={token} user={user} />
            <div className='search'>
                <div className='search-bar'>
                    <FontAwesomeIcon icon={faMagnifyingGlass} onClick={handleFindFriendByUsername} />
                    <input type='text' placeholder='Enter username...'
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
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
                    <div className='amount'>{countRequest}</div>
                </div>
            </div>
            <div className='list-chats'>
                {listFriends && listFriends.length > 0 ?
                    listFriends.map((item: any, index: number) => {
                        let contact = null;
                        if (tab !== 'accepts') {
                            contact = {
                                id: item?.userId?._id || item?.friendId?._id || item?.group?._id,
                                name: item?.userId?.name || item?.friendId?.name || item?.group?.groupName,
                                avatar: item?.userId?.imageUrl || item?.friendId?.imageUrl || null,
                                isGroup: tab === 'groups' ? true : false
                            };
                        }

                        return tab === 'accepts' ?
                            (<WaitingAcceptComponent contact={item} key={`friend-${index}`}
                                token={token} countRequest={countRequest} setCount={setCount}
                            />)
                            :
                            (<FriendComponent contact={contact} key={`friend-${index}`}
                                token={token}
                            />)
                    }) : <div className='no-content'>Empty</div>
                }
            </div>
            {openModalUserInfo && <AddFriendModal token={token} />}
            {openModalCreateGroup && <CreateGroupModal open={openModalCreateGroup}
                setOpen={setOpenModalCreateGroup} token={token} />}
        </div>
    );
}