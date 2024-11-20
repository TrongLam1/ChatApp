'use client'

import ChatComponent from "../chat/chatComponent";
import DetailComponent from "../detail/detailComponent";
import ListFriendsComponent from "../friends/listFriends/listFriendsComponent";
import './homePageComponent.scss';

export default function HomePageComponent(props: any) {

    const { user, token, listContacts } = props;

    return (
        <div className='home-page-container'>
            <ListFriendsComponent
                user={user} token={token} listContacts={listContacts}
            />
            <ChatComponent token={token} />
            <DetailComponent token={token} />
        </div>
    );
};