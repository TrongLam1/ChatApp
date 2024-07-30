import './UserInfo.scss';
import { useContext, useState } from 'react';
import avatar from '../../../assets/image/avatar.jpg';
import { UserContext } from '../../../context/UserContext';
import Dropdown from 'react-bootstrap/Dropdown';
import UserProfile from '../../Modal/UserInfo/UserProfile';

const UserInfo = (props) => {

    const { user } = props;

    const [open, setOpen] = useState(false);

    return (
        <>
            <div className='user-info-container'>
                <div className='user'>
                    <img src={user.avatar ? user.avatar : avatar} alt='' />
                    <h5>{user.username}</h5>
                </div>
                <div className='icons'>
                    <Dropdown>
                        <Dropdown.Toggle className='setting'>
                            <i className="fa-solid fa-ellipsis"></i>
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item onClick={() => setOpen(!open)}>Profile</Dropdown.Item>
                            <Dropdown.Item>Logout</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                    {/* <i className="fa-solid fa-video"></i>
                    <i className="fa-solid fa-pen-to-square"></i> */}
                </div>
            </div>
            <UserProfile username={user.username} open={open} setOpen={setOpen} />
        </>
    )
};

export default UserInfo;