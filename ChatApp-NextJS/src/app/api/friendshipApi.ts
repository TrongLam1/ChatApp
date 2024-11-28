import { sendRequest } from "@/utils/api";

export async function RequestFriend(token: string, body: IFriendship) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/request`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function AcceptFriend(token: string, body: IFriendship) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/accept`,
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function CancelFriend(token: string, body: IFriendship) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/cancel`,
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function FindFriend(token: string, friendId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/find-friend/${friendId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function GetListFriends(token: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/list-friends`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function GetListRequestFriends(token: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/list-request-friends`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function CountRequestFriends(token: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/count-request-friends`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function FindUsersByName(token: string, name: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/friendship/find-by-name/${name}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
        nextOption: {
            next: { tags: [`users-name-${name}`] }
        }
    });
};