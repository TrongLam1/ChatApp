'use client'

import { useState } from 'react';
import './detailComponent.scss';

export default function DetailComponent(props: any) {

    const [openModal, setOpenModal] = useState(false);
    const [userDetail, setUserDetail] = useState('');
    const [isShowMembers, setIsShowMembers] = useState(false);
    const [membersOfGroup, setMembersOfGroup] = useState([]);

    return (
        <>
            {/* <div className='detail-container'>
                {chatWith &&
                    <>
                        <div className='user'>
                            <img src={handleSetAvatar()} alt='avatar' />
                            <h3>{tab === 'friends' ? userDetail?.userName : userDetail?.groupName}</h3>
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
                            }
                            {tab === 'groups' &&
                                <button type='button' onClick={handleQuitOutGroup}>Quit</button>
                            }
                        </div>
                    </>
                }
            </div> */}
            {/* <InviteUsers open={openModal} setOpen={setOpenModal} groupId={chatWith} /> */}
        </>
    );
}