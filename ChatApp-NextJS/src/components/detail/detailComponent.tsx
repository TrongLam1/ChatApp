'use client'

import avatar from '@/assets/images/avatar.png';
import group from '@/assets/images/group.png';
import { useContactObject } from '@/providers/contactObjectProvider';
import { useTab } from '@/providers/tabProvider';
import { faCircleDown, faCircleUp, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { FindGroupById, GetListMembersGroup, QuitGroup } from '../../app/api/groupApi';
import InviteFriendComponent from '../modal/inviteFriend/inviteFriendComponent';
import './detailComponent.scss';
import MemberComponent from './memberGroup/memberComponent';

export default function DetailComponent(props: any) {
    const router = useRouter();

    const { token } = props;
    const { tab } = useTab();
    const { contactObject, setContactObject } = useContactObject();

    const [openModal, setOpenModal] = useState(false);
    const [isShowMembers, setIsShowMembers] = useState(false);
    const [membersOfGroup, setMembersOfGroup] = useState([]);

    useEffect(() => {
        setIsShowMembers(false);
        if (contactObject?.isGroup) {
            getListMembersGroup();
        };
    }, [contactObject]);

    const handleSetAvatar = () => {
        if (contactObject.isGroup) {
            return group;
        } else {
            return contactObject.avatar ?? avatar;
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
            router.refresh();
        } else { toast.error(res.message); }
    }

    return (
        <>
            <div className='detail-container'>
                {contactObject &&
                    <>
                        <div className='user'>
                            <img src={handleSetAvatar()} alt='avatar' />
                            <h3>{contactObject.name}</h3>
                        </div>
                        <div className='info'>
                            <div className='option'>
                                <div className='title'>
                                    <span>Chat Settings</span>
                                </div>
                            </div>
                            <div className='option'>
                                <div className='title'>
                                    <span>Privacy & help</span>
                                </div>
                            </div>
                            {tab === 'groups' &&
                                <div className='option'>
                                    <div className='title'>
                                        <div className='members-container'>
                                            <span>Members</span>
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
                            {tab === 'groups' &&
                                <button type='button' onClick={handleQuitOutGroup}>Quit</button>
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