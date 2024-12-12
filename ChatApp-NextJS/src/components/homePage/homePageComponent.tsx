'use client'

import { useTab } from "@/providers/tabProvider";
import ChatComponent from "../chat/chatComponent";
import DetailComponent from "../detail/detailComponent";
import ListFriendsComponent from "../friends/listFriends/listFriendsComponent";
import './homePageComponent.scss';
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { io } from "socket.io-client";
import { useContactObject } from "@/providers/contactObjectProvider";
import { toast } from "react-toastify";

const socket = io('http://localhost:3001');
export default function HomePageComponent(props: any) {
    const router = useRouter();
    const { user, token, listContacts, tab, countRequest } = props;

    const { setTab } = useTab();
    const { contactObject } = useContactObject();

    const [count, setCount] = useState<number>(0);

    useEffect(() => {
        if (!!user === false) router.push('/login');
    }, []);

    useEffect(() => {
        if (tab !== undefined) setTab(tab);
    }, []);

    useEffect(() => setCount(countRequest), [countRequest]);

    useEffect(() => {
        if (user) {
            socket.emit('joinNotification', user.id);

            socket.on('notification', (notify) => {
                if (notify.type === 'Request friend') {
                    toast.info('Bạn có lời mời kết bạn.');
                    setCount(count + 1);
                    return;
                };

                if (!contactObject) {
                    toast.success(<p>Tin nhắn mới từ: <br /> <strong>{notify.messageFrom}</strong></p>);
                    return;
                };

                if (!contactObject.isGroup) {
                    if (notify.subscribe !== contactObject?.channelId) {
                        toast.success(<p>Tin nhắn mới từ: <br /> <strong>{notify.messageFrom}</strong></p>);
                    } else if (notify.subscribe !== contactObject?.id) {
                        toast.success(<p>Tin nhắn mới từ: <br /> <strong>{notify.messageFrom}</strong></p>);
                    }
                }
            });

            return () => {
                socket.off('notification');
            };
        }
    }, [user, contactObject]);

    return (
        <div className='home-page-container'>
            <ListFriendsComponent
                user={user} token={token} listContacts={listContacts} countRequest={count}
                setCount={setCount}
            />
            <ChatComponent token={token} user={user} />
            <DetailComponent token={token} user={user} />
        </div>
    );
};