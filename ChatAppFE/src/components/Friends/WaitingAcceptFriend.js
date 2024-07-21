import avatar from '../../assets/image/avatar.jpg';
import { acceptAddFriend, denyAcceptFriend } from '../../services/FriendshipService';

const WaitingAcceptFriend = (props) => {

    const { item, getListChatsWaitingAccept } = props;

    const handleAcceptFriend = async () => {
        const res = await acceptAddFriend(item.id);
        if (res.status === 200) getListChatsWaitingAccept();
    };

    const handleDenyAcceptFriend = async () => {
        const res = await denyAcceptFriend(item.id);
        if (res.status === 200) getListChatsWaitingAccept();
    };

    return (
        <>
            <div className='friend'>
                <div className='accept-info'>
                    <img src={avatar} alt='' />
                    <div className='texts'>
                        <span>{item?.userName}</span>
                    </div>
                </div>
                <div className='action'>
                    <button type='button' className='accept' onClick={() => handleAcceptFriend()}>
                        <i className="fa-solid fa-circle-check"></i>
                    </button>
                    <button type='button' className='deny' onClick={() => handleDenyAcceptFriend()}>
                        <i className="fa-solid fa-circle-xmark"></i>
                    </button>
                </div>
            </div>
        </>
    );
};

export default WaitingAcceptFriend;