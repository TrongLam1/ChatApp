import './MemberGroup.scss';
import { useState } from 'react';
import avatar from '../../../assets/image/avatar.jpg';
import ConfirmModal from '../../Modal/ConfirmModal/ConfirmModal';
import { toast } from 'react-toastify';
import { sendRequestAddFriend } from '../../../services/FriendshipService';
import { removeMemberFromGroup } from '../../../services/GroupService';

const MemberGroup = (props) => {

    const { item, groupId, refreshMembers } = props;

    const [user, setUser] = useState(item);

    const [isOpen, setIsOpen] = useState(false);
    const [message, setMessage] = useState('');

    const closeModal = () => {
        setIsOpen(false);
    };

    const confirmModal = () => {
        setMessage(<>Are you sure remove <strong>"{user.userName}"</strong> from group?</>);
        setIsOpen(true);
    };

    const handleRequestFriend = async (userId) => {
        const res = await sendRequestAddFriend(userId);
        if (res && res.status === 200) {
            toast.success(res.message);
            setUser(res.data);
        } else {
            toast.error(res.message);
        }
    };

    const handleRemoveMember = async () => {
        const res = await removeMemberFromGroup(groupId, user.id);
        if (res && res.status === 200) {
            toast.success(res.data);
            refreshMembers();
        } else {
            toast.error(res.message);
        }
        closeModal();
    };

    return (
        <>
            <div className='member-group'>
                <div className='info'>
                    <img src={user?.avatar ? user?.avatar : avatar} alt='' />
                    <span>{user?.userName}</span>
                </div>
                <div className='action'>
                    <i className="fa-regular fa-circle-xmark remove-member"
                        onClick={confirmModal}></i>
                    {user?.status === null &&
                        <i className="fa-solid fa-user-plus request-friend"
                            onClick={() => handleRequestFriend(user.id)}
                        ></i>}
                </div>
            </div>
            <ConfirmModal isOpen={isOpen} closeModal={closeModal} message={message}
                onConfirm={handleRemoveMember} />
        </>
    );
};

export default MemberGroup;