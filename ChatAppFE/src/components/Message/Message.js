import './Message.scss';
import avatar from '../../assets/image/avatar.jpg';
import { UserContext } from '../../context/UserContext';
import { useContext } from 'react';

const Message = (props) => {

    const { message, tab } = props;

    const { user } = useContext(UserContext);

    const randomColor = () => {
        const color = "#" + Math.floor(Math.random() * 16777215).toString(16);
        return color;
    };

    return (
        <div className={`message ${message.sender === user.username ? 'own' : 'friend'}`}>
            {message.sender !== user.username && <img src={avatar} alt='' />}
            <div className='texts'>
                {tab === 'groups' && <div className='text-sender'>{message.sender}</div>}
                <p>{message.content}</p>
                <span>{message.createAt}</span>
            </div>
        </div>
    );
};

export default Message;