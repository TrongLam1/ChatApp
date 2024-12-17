import LoginComponent from "@/components/auth/loginComponent";

export const metadata = {
    title: 'Đăng nhập'
}

const LoginPage = ({ searchParams }: {
    searchParams: { [key: string]: string | string[] | undefined }
}) => {

    return (
        <LoginComponent userString={searchParams.response} />
    )
};

export default LoginPage;