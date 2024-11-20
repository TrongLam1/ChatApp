'use client'

import logo from '@/assets/images/logo.png';
import authenticate from '@/utils/actions';
import Image from 'next/image';
import Link from "next/link";
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { toast } from 'react-toastify';

export default function LoginComponent() {
    const router = useRouter();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loadingApi, setLoadingApi] = useState(false);

    const validateEmail = (email: string) => {
        // Regular expression for validating an email
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleLogin = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            toast.error("Missing email or password");
            return;
        }

        if (!validateEmail(email)) {
            toast.error("Invalid email");
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
                                    Sign In
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
                                                    placeholder="Password..." required />
                                                <label htmlFor="password" className="form-label">Password</label>
                                            </div>
                                        </div>
                                        <div className="col-12">
                                            <div className="d-grid my-3">
                                                <button className="btn btn-primary btn-lg" type="submit">
                                                    {loadingApi && <i className="fa-solid fa-sync fa-spin loader"></i>}
                                                    Sign In
                                                </button>
                                            </div>
                                        </div>
                                        <div className="col-12">
                                            <p className="m-0 text-secondary text-center">
                                                Do not have an account?
                                                <Link href='/register' className="link-primary text-decoration-none"> Sign Up</Link>
                                            </p>
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