import './UserInfo.scss';
import { useContext, useEffect, useState } from 'react';
import avatar from '../../../assets/image/avatar.jpg';
import { UserContext } from '../../../context/UserContext';
import Dropdown from 'react-bootstrap/Dropdown';
import UserProfile from '../../Modal/UserInfo/UserProfile';
import { useNavigate } from 'react-router-dom';

const UserInfo = (props) => {

    const navigate = useNavigate();

    const { user } = props;

    const [open, setOpen] = useState(false);

    const logOut = () => {
        navigate("/login");
    };

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
                            <Dropdown.Item onClick={logOut}>Logout</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
            </div>
            <UserProfile username={user.username} open={open} setOpen={setOpen} />
        </>
    )
};

export default UserInfo;