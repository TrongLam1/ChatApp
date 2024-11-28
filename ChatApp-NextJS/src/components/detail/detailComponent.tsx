'use client'

import { useEffect, useState } from 'react';
import avatar from '@/assets/images/avatar.png';
import group from '@/assets/images/group.png';
import './detailComponent.scss';
import { useContactObject } from '@/providers/contactObjectProvider';
import { useTab } from '@/providers/tabProvider';
import Image from 'next/image';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleDown, faCircleUp, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { GetListMembersGroup } from '../../app/api/groupApi';
import { toast } from 'react-toastify';
import MemberComponent from './memberGroup/memberComponent';
import InviteFriendComponent from '../modal/inviteFriend/inviteFriendComponent';

export default function DetailComponent(props: any) {

    const { token } = props;
    const { tab } = useTab();
    const { contactObject } = useContactObject();

    const [openModal, setOpenModal] = useState(false);
    const [isShowMembers, setIsShowMembers] = useState(false);
    const [membersOfGroup, setMembersOfGroup] = useState([]);

    useEffect(() => {
        if (contactObject?.isGroup) {
            getListMembersGroup();
        }
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

    return (
        <>
            <div className='detail-container'>
                {contactObject &&
                    <>
                        <div className='user'>
                            <Image src={handleSetAvatar()} alt='avatar' />
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
                                                membersOfGroup.map((item, index) => {
                                                    return (<MemberComponent
                                                        key={`members-${index}`} member={item} />)
                                                })
                                            }
                                        </div>
                                    }
                                </div>
                            }
                            {/* {tab === 'groups' &&
                                <button type='button' onClick={handleQuitOutGroup}>Quit</button>
                            } */}
                        </div>
                    </>
                }
            </div>
            {openModal && <InviteFriendComponent token={token} />}
        </>
    );
}