'use client'

import { useState } from 'react';
import avatar from '@/assets/images/avatar.png';
import group from '@/assets/images/group.png';
import './detailComponent.scss';
import { useContactObject } from '@/providers/contactObjectProvider';
import { useTab } from '@/providers/tabProvider';
import Image from 'next/image';

export default function DetailComponent(props: any) {

    const { tab } = useTab();
    const { contactObject } = useContactObject();

    const [openModal, setOpenModal] = useState(false);
    const [userDetail, setUserDetail] = useState('');
    const [isShowMembers, setIsShowMembers] = useState(false);
    const [membersOfGroup, setMembersOfGroup] = useState([]);

    const handleSetAvatar = () => {
        if (contactObject.isGroup) {
            return group;
        } else {
            return contactObject.avatar ?? avatar;
        }
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
                            {/* {tab === 'groups' &&
                                <div className='option'>
                                    <div className='title'>
                                        <div className='members-container'>
                                            <span>Members</span>
                                            <i className="fa-solid fa-user-plus"
                                                onClick={() => setOpenModal(!openModal)}
                                            ></i>
                                        </div>
                                        <i className={isShowMembers ? "fa-regular fa-circle-down" : "fa-regular fa-circle-up"}
                                            onClick={showMembersOfGroup}></i>
                                    </div>
                                    {isShowMembers &&
                                        <div className='list-members'>
                                            {membersOfGroup && membersOfGroup.length > 0 &&
                                                membersOfGroup.map((item, index) => {
                                                    return (<MemberGroup
                                                        key={`members-${index}`} item={item}
                                                        groupId={chatWith}
                                                        refreshMembers={getMembersOfGroup} />)
                                                })
                                            }
                                        </div>
                                    }
                                </div>
                            } */}
                            {/* {tab === 'groups' &&
                                <button type='button' onClick={handleQuitOutGroup}>Quit</button>
                            } */}
                        </div>
                    </>
                }
            </div>
            {/* <InviteUsers open={openModal} setOpen={setOpenModal} groupId={chatWith} /> */}
        </>
    );
}