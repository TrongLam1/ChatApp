export { };
// https://bobbyhadz.com/blog/typescript-make-types-global#declare-global-types-in-typescript

declare global {
    interface IRequest {
        url: string;
        method: string;
        body?: { [key: string]: any };
        queryParams?: any;
        useCredentials?: boolean;
        headers?: any;
        nextOption?: any;
    }

    interface IBackendRes<T> {
        error?: string | string[];
        message: string;
        statusCode: number | string;
        data?: T;
    }

    interface ILogin {
        user: {
            id: number;
            name: string;
            email: string;
            imageUrl: string;
        },
        access_token: string;
        refresh_token: string;
    }

    interface IProfile {
        username: string;
        phone: string;
    }

    interface IFriendship {
        friendId: string;
    }
}
