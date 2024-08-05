import './Friend.scss';
import avatar from '../../assets/image/avatar.jpg';
import { findChannelByUser } from '../../services/ChannelService';
import { WebSocketContext } from '../../context/WebSocketContext';
import { useContext, useEffect, useState } from 'react';
import { findUserById } from '../../services/UserService';
import { findGroupById } from '../../services/GroupService';

const Friend = (props) => {

    const { item, setChatWith, tab, handleShowContentForTab } = props;

    const { connectToChannel, handleSetReceiver, handleSetDescription,
        messageReceiver, notifyReceive } = useContext(WebSocketContext);

    const [friend, setFriend] = useState(item);

    useEffect(() => { }, [friend]);

    useEffect(() => {
        handleShowContentForTab();
    }, [messageReceiver, notifyReceive])

    const handleIsFriend = async (userId) => {
        const res = await findUserById(userId);
        return res?.data?.status === 'FRIEND' ? true : false;
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

    const formatTime = () => {
        if (item?.lastMessage?.createAt !== undefined) {
            const dateString = item?.lastMessage?.createAt;

            const date = new Date(dateString);

            // Extract and format the components
            const day = String(date.getDate()).padStart(2, '0');
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');

            return `${day}/${month} ${hours}:${minutes}`;
        }
    };

    return (
        <>
            <div className='friend'
                onClick={() => { tab === 'friends' ? handleSubscribeChannel(item?.id) : handleSubscribeGroup(item?.groupId) }}>
                <img src={item?.avatar || item?.image_url ? item?.avatar || item?.image_url : avatar} alt='' />
                <div className='texts'>
                    <div className='info'>
                        <div className='title-info'>
                            <span>{item.userName || item.groupName}</span>
                            <p>{formatTime()}</p>
                        </div>
                        {item.lastMessage &&
                            <div className='last-message'>
                                <p>{item.lastMessage.sender}:</p>
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