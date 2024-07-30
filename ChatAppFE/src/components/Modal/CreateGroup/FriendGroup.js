import './FriendGroup.scss';
import avatar from '../../../assets/image/avatar.jpg';

const FriendGroup = (props) => {

    const { item, setListAddGroup } = props;

    const handleAddFriendToGroup = (e) => {
        const isChecked = e.target.checked;

        if (isChecked) {
            // If checked, add the item to the list
            setListAddGroup(prevList => [...new Set([...prevList, item.id])]);
        } else {
            // If unchecked, remove the item from the list
            setListAddGroup(prevList => prevList.filter(id => id !== item.id));
        }
    };

    return (
        <div className="friend-group-item">
            <input type="checkbox" id={item.id} onClick={handleAddFriendToGroup} />
            <label htmlFor={item.id}>
                <img src={item.avatar ? item.avatar : avatar} alt='' />
                <div className='texts'>
                    <div className='info'>
                        <p>{item.userName}</p>
                    </div>
                </div>
            </label>
        </div>
    );
};

export default FriendGroup;