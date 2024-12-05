import { sendRequest, sendRequestFile } from "@/utils/api";

export async function ChannelSendMessage(token: string, body: ISendMessage) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/channel-messages/post-message`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function ChannelSendImage(token: string, body: ISendMessage) {
    return await sendRequestFile<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/channel-messages/post-image`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function ChannelGetMessages(token: string, channelId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/channel-messages/get-messages/${channelId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` }
    });
};

export async function GroupSendMessage(token: string, body: ISendMessage) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/group-messages/post-message`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function GroupSendImage(token: string, body: ISendMessage) {
    return await sendRequestFile<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/group-messages/post-image`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function GroupGetMessages(token: string, groupId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/group-messages/get-messages/${groupId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` }
    });
};