import './Friend.scss';
import avatar from '../../assets/image/avatar.jpg';
import { findChannelByUser } from '../../services/ChannelService';
import { WebSocketContext } from '../../context/WebSocketContext';
import { useContext, useEffect, useState } from 'react';
import { findUserById } from '../../services/UserService';
import { findGroupById } from '../../services/GroupService';

const Friend = (props) => {

    const { item, setChatWith, tab } = props;

    const { connectToChannel, handleSetReceiver, handleSetDescription } = useContext(WebSocketContext);

    const [friend, setFriend] = useState(item);

    useEffect(() => { }, [friend]);

    const handleIsFriend = async (userId) => {
        const res = await findUserById(userId);
        return res.data.status === 'FRIEND' ? true : false;
    };

    const handleSubscribeChannel = async (userId) => {
        const isFriend = await handleIsFriend(userId);
        if (isFriend) {
            const res = await findChannelByUser(userId);
            if (res && res.status === 200) {
                connectToChannel(res.data);
                handleSetReceiver(item?.userName);
                setChatWith(userId);
            }
        }
    };

    const handleSubscribeGroup = async (groupId) => {
        const res = await findGroupById(groupId);
        if (res && res.status === 200) {
            connectToChannel(res.data.group.groupId);
            handleSetReceiver(res.data.group.groupName);
            setChatWith(res.data.group.groupId);
            handleSetDescription(res.data.totalMembers);
        }
    };

    return (
        <>
            <div className='friend'
                onClick={() => { tab === 'friends' ? handleSubscribeChannel(item?.id) : handleSubscribeGroup(item?.groupId) }}>
                <img src={item?.avatar || item?.image_url ? item?.avatar || item?.image_url : avatar} alt='' />
                <div className='texts'>
                    <div className='info'>
                        <span>{item.userName || item.groupName}</span>
                        {item.lastMessage &&
                            <div className='last-message'>
                                <p>{tab === 'friends' && item.lastMessage.sender}:</p>
                                <p>{item.lastMessage.content}</p>
                            </div>
                        }
                    </div>
                </div>
            </div>
        </>
    );
};

export default Friend;