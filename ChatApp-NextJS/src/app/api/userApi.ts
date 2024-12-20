'use server'

import { sendRequest, sendRequestFile } from "@/utils/api";

export async function Register(username: string, email: string, password: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/auth/register`,
        method: 'POST',
        body: {
            username, email, password
        }
    });
}

export async function GetProfile(token: string) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/users`,
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
    });
}

export async function UpdateProfile(token: string, body: IProfile) {
    return await sendRequest<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/users/update-profile`,
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` },
        body
    });
}

export async function ChangeAvatar(token: string, formData: any) {
    return await sendRequestFile<IBackendRes<any>>({
        url: `${process.env.NEXT_PUBLIC_BACKEND_URL}/users/change-avatar`,
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` },
        body: formData
    });
}

export async function LoginWithGoogle() {
    return `${process.env.NEXT_PUBLIC_BACKEND_URL}/auth/google/login`;
}