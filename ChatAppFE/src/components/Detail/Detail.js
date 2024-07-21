import './Detail.scss';
import { findUserById } from '../../services/UserService';
import { useEffect, useState } from 'react';

const Detail = (props) => {

    const { chatWith } = props;

    const [userDetail, setUserDetail] = useState('');

    // useEffect(() => {
    //     fetchUserDetail();
    // }, [chatWith]);

    // const fetchUserDetail = async () => {
    //     const res = await findUserById(chatWith);
    //     if (res && res.status === 200) {
    //         setUserDetail(res.data);
    //     }
    // };

    const handleBlockUser = async () => {
        console.log("block user id ", userDetail.id);
    };

    return (
        <div className='detail-container'>
            {chatWith &&
                <>
                    <div className='user'>
                        <img src='' alt='' />
                        <h3>{userDetail.userName}</h3>
                    </div>
                    <div className='info'>
                        <div className='option'>
                            <div className='title'>
                                <span>Chat Settings</span>
                                <img />
                            </div>
                        </div>
                        <div className='option'>
                            <div className='title'>
                                <span>Privacy & help</span>
                                <img />
                            </div>
                        </div>
                        <div className='option'>
                            <div className='title'>
                                <span>Shared photos</span>
                                <img />
                            </div>
                            <div className='photos'>
                                <div className='photo-item'>
                                    <div className='photo-detail'>
                                        <img src='' alt='' />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className='option'>
                            <div className='title'>
                                <span>Shared files</span>
                                <img />
                            </div>
                        </div>
                        <button type='button' onClick={handleBlockUser}>Block user</button>
                        <button type='button' className='logout'>Logout</button>
                    </div>
                </>
            }
        </div>
    )
};

export default Detail;