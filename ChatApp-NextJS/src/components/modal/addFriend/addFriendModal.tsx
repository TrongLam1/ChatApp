'use client'

import { FormEvent, useEffect, useState } from 'react';
import './addFriendModal.scss';
import UserAddInfo from './userAddInfo';
import { FindUsersByName } from '@/app/api/friendshipApi';

export default function AddFriendModal(props: any) {

    const { token } = props;

    const [nameSearch, setNameSearch] = useState('');
    const [users, setUsers] = useState<any>();

    useEffect(() => { }, [users]);

    const handleFindByName = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const res = await FindUsersByName(token, nameSearch);
        if (res.statusCode === 200 && res.data) {
            setUsers(res.data);
        }
    };

    return (
        <div className='add-user-modal'>
            <form onSubmit={(e) => handleFindByName(e)}>
                <input type='text' placeholder='Nhập tên người dùng ...' value={nameSearch}
                    onChange={(e) => setNameSearch(e.target.value)} />
                <button className='search-btn'>Tìm</button>
            </form>
            <div className='list-users'>
                {users && users.length > 0 ?
                    users.map((user: any, index: number) => {
                        return (<UserAddInfo key={`user-${index}`} user={user} token={token} />)
                    }) : <div>Không có thông tin người dùng.</div>
                }
            </div>
        </div>
    );
};