import './Detail.scss';
import avatar from '../../assets/image/avatar.jpg';
import group from '../../assets/image/group.png';
import Friend from '../Friends/Friend';
import { findUserById } from '../../services/UserService';
import { findGroupById, fetchMembersOfGroup } from '../../services/GroupService';
import { useEffect, useState } from 'react';

const Detail = (props) => {

    const { chatWith, tab } = props;

    const [userDetail, setUserDetail] = useState('');
    const [isShowMembers, setIsShowMembers] = useState(false);
    const [membersOfGroup, setMembersOfGroup] = useState([]);

    useEffect(() => {
        if (tab === 'friends') {
            fetchUserDetail();
        } else {
            fetchGroupDetail();
        }
    }, [chatWith]);

    const fetchUserDetail = async () => {
        const res = await findUserById(chatWith);
        if (res && res.status === 200) {
            setUserDetail(res.data);
        }
    };

    const fetchGroupDetail = async () => {
        const res = await findGroupById(chatWith);
        if (res && res.status === 200) {
            setUserDetail(res.data.group);
        }
    };

    const handleSetAvatar = () => {
        if (tab === 'friends') {
            return userDetail.avatar ? userDetail.avatar : avatar;
        } else if (tab === 'groups') {
            return group;
        }
    };

    const getMembersOfGroup = async () => {
        const res = await fetchMembersOfGroup(chatWith);
        if (res && res.status === 200) {
            setMembersOfGroup(res.data);
        }
    };

    const showMembersOfGroup = () => {
        setIsShowMembers(!isShowMembers);
        getMembersOfGroup();
    };

    const handleBlockUser = async () => {
        console.log("block user id ", userDetail.id);
    };

    return (
        <>
            <div className='detail-container'>
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
                            {/* {tab === 'groups' &&
                                
                            } */}
                            <div className='option'>
                                <div className='title'>
                                    <span>Members</span>
                                    <i className={isShowMembers ? "fa-regular fa-circle-down" : "fa-regular fa-circle-up"}
                                        onClick={showMembersOfGroup}></i>
                                </div>
                                {isShowMembers &&
                                    <div className='list-members'>
                                        {membersOfGroup && membersOfGroup.length > 0 &&
                                            membersOfGroup.map((item, index) => {
                                                return (<Friend key={`members-${index}`} item={item.user} />)
                                            })
                                        }
                                    </div>
                                }
                            </div>
                            {tab === 'friends' ?
                                <button type='button' onClick={handleBlockUser}>Block user</button> :
                                <button type='button' onClick={handleBlockUser}>Quit</button>
                            }
                        </div>
                    </>
                }
            </div>
        </>
    )
};

export default Detail;