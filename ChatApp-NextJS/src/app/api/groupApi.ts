import { sendRequest } from "@/utils/api";

export async function CreateGroup(token: string, body: ICreateGroup) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/create`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
};

export async function AddMember(token: string, groupId: string, memberId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/add-member?groupId=${groupId}&memberId=${memberId}`,
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function FindGroupById(token: string, groupId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/find-group/${groupId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
}

export async function GetListMembersGroup(token: string, groupId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/members/${groupId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function GetListFriendsInvite(token: string, groupId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/friends-invite/${groupId}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function GetListGroupsOfUser(token: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/list-groups-by-user`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function RemoveMember(token: string, groupId: string, memberId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/remove-member?groupId=${groupId}&memberId=${memberId}`,
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
    });
};

export async function QuitGroup(token: string, groupId: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/groups/quit/${groupId}`,
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
    });
};