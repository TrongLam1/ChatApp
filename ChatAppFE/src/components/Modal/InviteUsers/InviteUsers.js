import '../CreateGroup/CreateGroup.scss';
import { useEffect, useState } from 'react';
import FriendGroup from '../CreateGroup/FriendGroup';
import { findUserByEmail } from '../../../services/UserService';
import { addUsersToGroup, fetchFriendsToInvite } from '../../../services/GroupService';

const InviteUsers = (props) => {

    const { groupId, open, setOpen } = props;

    const [nameSearch, setNameSearch] = useState('');
    const [listFriends, setListFriends] = useState([]);
    const [listAddGroup, setListAddGroup] = useState([]);

    useEffect(() => {
        getListFriends();

        return (() => setListAddGroup([]));
    }, [open]);

    const handleFindByName = async (e) => {
        e.preventDefault();

        if (nameSearch === '') { getListFriends(); return }

        const res = await findUserByEmail(nameSearch);
        if (res && res.status === 200) {
            setListFriends([res.data]);
        } else {
            setListFriends([]);
        }
    };

    const getListFriends = async () => {
        const res = await fetchFriendsToInvite(groupId);
        if (res && res.status === 200) {
            setListFriends(res.data);
        }
    };

    const handleAddUsersToGroup = async () => {
        const res = await addUsersToGroup(groupId, listAddGroup);
        if (res && res.status === 200) setOpen(false);
    };

    return (
        <>
            {open &&
                <div className='modal-create-group'>
                    <h4>Add Friends To Group</h4>
                    <form onSubmit={(e) => handleFindByName(e)}>
                        <input type='text' placeholder='Enter username...' value={nameSearch}
                            onChange={(e) => setNameSearch(e.target.value)} />
                        <button className='search-btn'>Search</button>
                    </form>
                    <div className='user'>
                        {listFriends && listFriends.length > 0 &&
                            listFriends.map((item, index) => {
                                return (<FriendGroup key={`item-${index}`} item={item}
                                    setListAddGroup={setListAddGroup} />)
                            })
                        }
                    </div>
                    <div className='btn-add'>
                        <button onClick={handleAddUsersToGroup}>Add</button>
                    </div>
                </div>
            }
        </>
    );
};

export default InviteUsers;