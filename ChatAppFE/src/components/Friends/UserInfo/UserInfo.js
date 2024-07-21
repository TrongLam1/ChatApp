import './UserInfo.scss';
import { useContext } from 'react';
import avatar from '../../../assets/image/avatar.jpg';
import { UserContext } from '../../../context/UserContext';

const UserInfo = (props) => {

    const { user } = props;

    return (
        <div className='user-info-container'>
            <div className='user'>
                <img src={avatar} alt='' />
                <h5>{user.username}</h5>
            </div>
            <div className='icons'>
                <i className="fa-solid fa-ellipsis"></i>
                <i className="fa-solid fa-video"></i>
                <i className="fa-solid fa-pen-to-square"></i>
            </div>
        </div>
    )
};

export default UserInfo;