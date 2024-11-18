import { AuthError } from "next-auth";

export class CustomAuthError extends AuthError {
    static type: string;

    constructor(message?: any) {
        super();
        this.type = message;
    }
}

export class InvalidEmailPasswordError extends AuthError {
    static type: string = "Email hoặc mật khẩu không đúng.";
}

export class NotFoundUserError extends AuthError {
    static type: string = "Tài khoản không tồn tại.";
}