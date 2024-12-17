'use client'

import google from '@/assets/images/google.png';
import logo from '@/assets/images/logo.png';
import authenticate from '@/utils/actions';
import { CSpinner } from '@coreui/react';
import Image from 'next/image';
import Link from "next/link";
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import style from './loginComponent.module.scss';
import { LoginWithGoogle } from '@/app/api/userApi';

export default function LoginComponent(props: any) {
    const router = useRouter();

    const { userString } = props;
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loadingApi, setLoadingApi] = useState(false);

    useEffect(() => {
        if (userString !== undefined) handleLoginGoogle();
    }, []);

    const validateEmail = (email: string) => {
        // Regular expression for validating an email
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleLogin = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            toast.error("Email hoặc mật khẩu không được để trống.");
            return;
        }

        if (!validateEmail(email)) {
            toast.error("Email không hợp lệ.");
            return;
        }

        setLoadingApi(true);

        const res = await authenticate(email, password);

        if (res.err) {
            toast.error(res.error);
        } else {
            router.push("/");
        }

        setLoadingApi(false);
    }

    const redirectLoginGoogle = async () => {
        const url = await LoginWithGoogle();
        router.push(url);
    };

    const handleLoginGoogle = async () => {
        await authenticate(userString, "");
        router.push("/");
    };

    return (
        <section className="bg-light py-3 py-md-5">
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-12 col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4">
                        <div className="card border border-light-subtle rounded-3 shadow-sm">
                            <div className="card-body p-3 p-md-4 p-xl-5">
                                <div className="text-center mb-3">
                                    <div>
                                        <Image src={logo} alt="Logo" width="80" height="80" />
                                    </div>
                                </div>
                                <h2 className="fs-2 fw-bold text-center text-secondary mb-4">
                                    Đăng nhập
                                </h2>
                                <form onSubmit={(e) => handleLogin(e)}>
                                    <div className="row gy-2 overflow-hidden">
                                        <div className="col-12">
                                            <div className="form-floating mb-3">
                                                <input type="email" className="form-control" name="email" id="email" placeholder="name@example.com"
                                                    onChange={(e) => setEmail(e.target.value)}
                                                    required />
                                                <label htmlFor="email" className="form-label">Email</label>
                                            </div>
                                        </div>
                                        <div className="col-12">
                                            <div className="form-floating mb-3">
                                                <input
                                                    type="password"
                                                    className="form-control"
                                                    name="password" id="password"
                                                    onChange={(e) => setPassword(e.target.value)}
                                                    placeholder="Mật khẩu ..." required />
                                                <label htmlFor="password" className="form-label">Mật khẩu</label>
                                            </div>
                                        </div>
                                        <div className="col-12">
                                            <div className="d-grid my-3">
                                                <button className="btn btn-primary btn-lg" type="submit" disabled={loadingApi ? true : false}>
                                                    {loadingApi &&
                                                        <CSpinner color="light" size="sm" className='me-3' style={{ width: '1.3rem', height: '1.3rem' }} />}
                                                    Đăng nhập
                                                </button>
                                            </div>
                                        </div>
                                        <div className="col-12">
                                            <p className="m-0 text-secondary text-center">
                                                Bạn chưa có tài khoản?
                                                <Link href='/register' className="link-primary text-decoration-none"> Đăng ký.</Link>
                                            </p>
                                        </div>
                                        <div className={style.gSignInButton}
                                            onClick={redirectLoginGoogle}>
                                            <div className={style.contentWrapper}>
                                                <div className={style.logoWrapper}>
                                                    <Image src={google} fill alt='google' />
                                                </div>
                                                <span className={style.textContainer}>
                                                    <span>Đăng nhập với Google</span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}