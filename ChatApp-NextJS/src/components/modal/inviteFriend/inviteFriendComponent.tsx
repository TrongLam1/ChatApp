'use client'

import { GetListFriendsInvite } from "@/app/api/groupApi";
import { useContactObject } from "@/providers/contactObjectProvider";
import { FormEvent, useEffect, useState } from "react";
import MemberGroupComponent from "../createGroup/memberGroupComponent";

export default function InviteFriendComponent(props: any) {
    const { token } = props;

    const { contactObject } = useContactObject();

    const [nameSearch, setNameSearch] = useState('');
    const [listFriends, setListFriends] = useState([]);
    const [listFriendsSearch, setListFriendsSearch] = useState([]);
    const [listAddGroup, setListAddGroup] = useState([]);

    useEffect(() => {
        getListFriends();
    }, []);

    const handleFindByName = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (nameSearch !== '') {
            const search: any = listFriends
                .filter((friend: any) => friend.userId.name.includes(nameSearch))
                .map((friend: any) => ({ friend }));

            setListFriendsSearch(search);
        } else {
            setListFriendsSearch(listFriends);
        }

    };

    const getListFriends = async () => {
        const res = await GetListFriendsInvite(token, contactObject.id);
        if (res.statusCode === 200) {
            setListFriends(res.data);
            setListFriendsSearch(res.data);
        }
    };

    const handleAddUsersToGroup = async () => {
        console.log(listAddGroup);
    };

    return (
        <div className='modal-create-group'>
            <h4>Add Friends To Group</h4>
            <form onSubmit={(e) => handleFindByName(e)}>
                <input type='text' placeholder='Enter username...' value={nameSearch}
                    onChange={(e) => setNameSearch(e.target.value)} />
                <button className='search-btn'>Search</button>
            </form>
            <div className='user'>
                {listFriendsSearch && listFriendsSearch.length > 0 &&
                    listFriendsSearch.map((item: any, index: number) => {
                        return (<MemberGroupComponent key={`item-${index}`} member={item}
                            setListAddGroup={setListAddGroup} />)
                    })
                }
            </div>
            <div className='btn-add'>
                <button onClick={handleAddUsersToGroup}>Add</button>
            </div>
        </div>
    );
}