'use client'

import avatarImg from '@/assets/images/avatar.png';
import group from '@/assets/images/group.png';
import { useContactObject } from '@/providers/contactObjectProvider';
import { useTab } from '@/providers/tabProvider';
import { faCircleDown, faCircleUp, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { FindGroupById, GetListMembersGroup, QuitGroup } from '../../app/api/groupApi';
import InviteFriendComponent from '../modal/inviteFriend/inviteFriendComponent';
import './detailComponent.scss';
import MemberComponent from './memberGroup/memberComponent';

export default function DetailComponent(props: any) {
    const { token } = props;
    const { contactObject, setContactObject } = useContactObject();

    const [avatar, setAvatar] = useState<any>();
    const [openModal, setOpenModal] = useState(false);
    const [isShowMembers, setIsShowMembers] = useState(false);
    const [membersOfGroup, setMembersOfGroup] = useState([]);

    useEffect(() => {
        setIsShowMembers(false);
        if (contactObject && contactObject?.isGroup) {
            getListMembersGroup();
        };
        handleSetAvatar();
    }, [contactObject]);

    const handleSetAvatar = () => {
        if (contactObject?.isGroup) {
            setAvatar(group);
        } else {
            setAvatar(contactObject?.avatar ?? avatarImg);
        }
    };

    const getListMembersGroup = async () => {
        const res = await GetListMembersGroup(token, contactObject.id);
        if (res.statusCode === 200) {
            setMembersOfGroup(res.data);
        } else { toast.error(res.message); }
    };

    const updateInfoGroup = async () => {
        const res = await FindGroupById(token, contactObject.id);
        if (res.statusCode === 200) {
            setContactObject({
                id: res.data.group.id,
                name: res.data.group.groupName,
                isGroup: true,
                members: res.data.members
            });
        }
    };

    const handleQuitOutGroup = async () => {
        const res = await QuitGroup(token, contactObject.id);
        if (res.statusCode === 200) {
            toast.success(res.data);
            setContactObject(null);
        } else { toast.error(res.message); }
    }

    return (
        <>
            <div className='detail-container'>
                {contactObject &&
                    <>
                        <div className='user'>
                            <div className='avatar'>
                                <Image src={avatar} fill alt='avatar' />
                            </div>
                            <h3>{contactObject.name}</h3>
                        </div>
                        <div className='info'>
                            <div className='option'>
                                <div className='title'>
                                    <span>Cài đặt</span>
                                </div>
                            </div>
                            <div className='option'>
                                <div className='title'>
                                    <span>Riêng tư</span>
                                </div>
                            </div>
                            {contactObject.isGroup &&
                                <div className='option'>
                                    <div className='title'>
                                        <div className='members-container'>
                                            <span>Thành viên</span>
                                            <FontAwesomeIcon icon={faUserPlus}
                                                onClick={() => setOpenModal(!openModal)} />
                                        </div>
                                        {isShowMembers ?
                                            <FontAwesomeIcon icon={faCircleDown}
                                                onClick={() => setIsShowMembers(!isShowMembers)} /> :
                                            <FontAwesomeIcon icon={faCircleUp}
                                                onClick={() => setIsShowMembers(!isShowMembers)} />}
                                    </div>
                                    {isShowMembers &&
                                        <div className='list-members'>
                                            {membersOfGroup && membersOfGroup.length > 0 &&
                                                membersOfGroup.map((item: any, index: number) => {
                                                    return (<MemberComponent key={`members-${index}`}
                                                        token={token} member={item}
                                                        updateInfoGroup={updateInfoGroup}
                                                        setMembersOfGroup={setMembersOfGroup} />)
                                                })
                                            }
                                        </div>
                                    }
                                </div>
                            }
                            {contactObject.isGroup &&
                                <button type='button' onClick={handleQuitOutGroup}>Thoát</button>
                            }
                        </div>
                    </>
                }
            </div>
            {openModal && <InviteFriendComponent token={token} setIsShowMembers={setIsShowMembers}
                getListMembersGroup={getListMembersGroup} setOpen={setOpenModal}
                updateInfoGroup={updateInfoGroup} />}
        </>
    );
}