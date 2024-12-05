'use client'

import { useTab } from "@/providers/tabProvider";
import ChatComponent from "../chat/chatComponent";
import DetailComponent from "../detail/detailComponent";
import ListFriendsComponent from "../friends/listFriends/listFriendsComponent";
import './homePageComponent.scss';
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function HomePageComponent(props: any) {
    const router = useRouter();
    const { user, token, listContacts, tab, countRequest } = props;

    const { setTab } = useTab();

    useEffect(() => {
        if (!!user === false) router.push('/login');
    }, []);

    useEffect(() => {
        if (tab !== undefined) setTab(tab);
    }, []);

    return (
        <div className='home-page-container'>
            <ListFriendsComponent
                user={user} token={token} listContacts={listContacts} countRequest={countRequest}
            />
            <ChatComponent token={token} user={user} />
            <DetailComponent token={token} user={user} />
        </div>
    );
};