'use client'

import './createGroupModal.scss';
import { useEffect, useState } from "react";
import MemberGroupComponent from './memberGroupComponent';
import { GetListFriends } from '@/app/api/friendshipApi';
import { CreateGroup } from '@/app/api/groupApi';
import { toast } from 'react-toastify';

export default function CreateGroupModal(props: any) {

    const { open, setOpen, token } = props;

    const [nameSearch, setNameSearch] = useState('');
    const [user, setUser] = useState('');
    const [listFriends, setListFriends] = useState([]);
    const [listAddGroup, setListAddGroup] = useState([]);
    const [groupName, setGroupName] = useState('');

    useEffect(() => {
        getListFriends();
    }, [])

    const handleFindByName = async (e: any) => {
        e.preventDefault();


    }

    const getListFriends = async () => {
        const res = await GetListFriends(token);
        if (res.statusCode === 200) {
            setListFriends(res.data);
        }
    }

    const handleCreateNewGroup = async () => {
        if (listAddGroup.length <= 1) {
            toast.error("Tối thiểu 2 người bạn để tạo nhóm.");
            return;
        }

        const body = {
            groupName: groupName,
            memberIds: listAddGroup
        }

        const res = await CreateGroup(token, body);
        if (res.statusCode === 201) {
            toast.success('Tạo nhóm thành công.');
            setOpen(false);
        } else {
            toast.error(res.message);
        }
    }

    return (
        <>
            {open &&
                (<div className='modal-create-group'>
                    <h4>Tạo nhóm mới</h4>
                    <form onSubmit={(e) => handleFindByName(e)}>
                        <input type='text' placeholder='Nhập tên người dùng ...' value={nameSearch}
                            onChange={(e) => setNameSearch(e.target.value)} />
                        <button className='search-btn'>Tìm</button>
                    </form>
                    <div className='user'>
                        {listFriends && listFriends.length > 0 &&
                            listFriends.map((item, index) => {
                                return (<MemberGroupComponent key={`item-${index}`} member={item}
                                    setListAddGroup={setListAddGroup} />)
                            })
                        }
                    </div>
                    <div className='btn-create'>
                        <input type='text' value={groupName}
                            placeholder="Nhập tên nhóm ..."
                            onChange={(e) => setGroupName(e.target.value)} />
                        <button onClick={handleCreateNewGroup}>Tạo</button>
                    </div>
                </div>)
            }
        </>
    );
}