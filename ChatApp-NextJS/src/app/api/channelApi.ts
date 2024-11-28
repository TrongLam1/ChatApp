import { sendRequest } from "@/utils/api";

export async function FindChannel(token: string, friendId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/channels/find-channel/${friendId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};