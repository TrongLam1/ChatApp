'use client'

import { useState } from "react";
import './homePageComponent.scss';
import ListFriendsComponent from "../friends/listFriends/listFriendsComponent";
import ChatComponent from "../chat/chatComponent";
import DetailComponent from "../detail/detailComponent";

export default function HomePageComponent() {

    const [tab, setTab] = useState('friends');
    const [chatWith, setChatWith] = useState('');

    return (
        <div className='home-page-container'>
            <ListFriendsComponent
                setChatWith={setChatWith}
                tab={tab} setTab={setTab}
            />
            <ChatComponent setChatWith={setChatWith} chatWith={chatWith} tab={tab} />
            <DetailComponent chatWith={chatWith} tab={tab} />
        </div>
    );
};