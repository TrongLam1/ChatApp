import './CreateGroup.scss';
import FriendGroup from './FriendGroup';
import { useEffect, useState } from 'react';
import { findUserByEmail } from '../../../services/UserService';
import { fetchListFriends } from '../../../services/FriendshipService';
import { createNewGroup } from '../../../services/GroupService';

const CreateGroup = (props) => {

    const { open, setOpen } = props;

    const [nameSearch, setNameSearch] = useState('');
    const [user, setUser] = useState('');
    const [listFriends, setListFriends] = useState([]);
    const [listAddGroup, setListAddGroup] = useState([]);
    const [groupName, setGroupName] = useState('');

    useEffect(() => {
        getListFriends();
    }, []);

    const handleFindByName = async (e) => {
        e.preventDefault();

        const res = await findUserByEmail(nameSearch);
        if (res && res.status === 200) {
            setUser(res.data);
        } else {
            setUser('');
        }
    };

    const getListFriends = async () => {
        const res = await fetchListFriends();
        if (res && res.status === 200) {
            setListFriends(res.data);
        }
    };

    const handleCreateNewGroup = async () => {
        if (listAddGroup.length < 3) {
            alert("> 2");
            return;
        }
        const request = {
            groupName: groupName,
            listFriends: listAddGroup
        };
        const res = await createNewGroup(request);
        if (res && res.status === 200) setOpen(false);
    };

    return (
        <>
            {open &&
                (<div className='modal-create-group'>
                    <h4>Create New Group</h4>
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
                    <div className='btn-create'>
                        <input type='text' value={groupName}
                            placeholder="Enter group's name..."
                            onChange={(e) => setGroupName(e.target.value)} />
                        <button onClick={handleCreateNewGroup}>Create</button>
                    </div>
                </div>)
            }
        </>
    );
};

export default CreateGroup;