import Chat from '../Chat/Chat';
import ListFriends from '../Friends/ListFriends';
import Detail from '../Detail/Detail';
import { UserContext } from '../../context/UserContext';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.scss';

const HomePage = (props) => {

    const navigate = useNavigate();
    const { user } = useContext(UserContext);
    const [tab, setTab] = useState('friends');
    const [chatWith, setChatWith] = useState('');

    const handleSetChatWithUser = (userId) => {
        setChatWith(userId);
    };

    // useEffect(() => {
    //     checkLogin();
    // }, []);

    // const checkLogin = () => {
    //     if (user.auth === false) {
    //         navigate("/login");
    //     };
    // };

    return (
        <div className='home-page-container'>
            <ListFriends
                setChatWith={setChatWith}
                tab={tab} setTab={setTab}
            />
            <Chat setChatWith={setChatWith} tab={tab} />
            <Detail chatWith={chatWith} tab={tab} />
        </div>
    );
};

export default HomePage;