'use client'

import { AddMembers, GetListFriendsInvite } from "@/app/api/groupApi";
import { useContactObject } from "@/providers/contactObjectProvider";
import { FormEvent, useEffect, useState } from "react";
import { toast } from "react-toastify";
import MemberGroupComponent from "../createGroup/memberGroupComponent";

export default function InviteFriendComponent(props: any) {
    const { token, getListMembersGroup, setOpen, setIsShowMembers, updateInfoGroup } = props;

    const { contactObject } = useContactObject();

    const [nameSearch, setNameSearch] = useState('');
    const [listFriends, setListFriends] = useState([]);
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

            setListFriends(search);
        } else {
            await getListFriends();
        }

    };

    const getListFriends = async () => {
        const res = await GetListFriendsInvite(token, contactObject.id);
        if (res.statusCode === 200) {
            setListFriends(res.data);
        }
    };

    const handleAddUsersToGroup = async () => {
        const body: IAddMembers = {
            groupId: contactObject.id,
            memberIds: listAddGroup
        };

        const res = await AddMembers(token, body);
        if (res.statusCode === 201) {
            toast.success("Add members successfully.");
            getListMembersGroup();
            setOpen(false);
            setIsShowMembers(false);
            updateInfoGroup();
        } else {
            toast.error(res.message);
        }
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
                {listFriends && listFriends.length > 0 &&
                    listFriends.map((item: any, index: number) => {
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