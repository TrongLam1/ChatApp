import './Login.scss';
import { useNavigate, Link } from 'react-router-dom';
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { signUp } from '../../services/AuthenticationService';

const SignUp = (props) => {

    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [username, setUsername] = useState('');
    const [loadingApi, setLoadingApi] = useState(false);

    const handleSignUp = async (e) => {
        e.preventDefault();

        if (!email || !password || !username) {
            toast.error("Vui lòng không để trống.");
            return;
        }

        setLoadingApi(true);

        let res = await signUp(username, email, password);
        if (res) {
            toast.success("Đăng kí tài khoản thành công.")
            navigate("/login");
        } else {
            if (res && res.status === 400) {
                let index = res.message.lastIndexOf(":");
                let errorMessage = res.message.substring(+index + 1);
                toast.error(errorMessage);
            }
        }

        setLoadingApi(false);
    };

    return (
        <>
            <section className="bg-light py-3 py-md-5">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-12 col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4">
                            <div className="card border border-light-subtle rounded-3 shadow-sm">
                                <div className="card-body p-3 p-md-4 p-xl-5">
                                    <div className="text-center mb-3">
                                        <div>
                                            <img src="" alt="Logo" width="80" height="80" />
                                        </div>
                                    </div>
                                    <h2 className="fs-2 fw-bold text-center text-secondary mb-4">
                                        Đăng kí
                                    </h2>
                                    <form onSubmit={(e) => handleSignUp(e)}>
                                        <div className="row gy-2 overflow-hidden">
                                            <div className="col-12">
                                                <div className="form-floating mb-3">
                                                    <input type="text" className="form-control" name="username" placeholder='Nguyễn Văn A' id="username"
                                                        onChange={(e) => setUsername(e.target.value)}
                                                        required />
                                                    <label htmlFor="username" className="form-label">
                                                        Họ và tên
                                                    </label>
                                                </div>
                                            </div>
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
                                                    <input type="password" className="form-control" name="password" id="password"
                                                        onChange={(e) => setPassword(e.target.value)}
                                                        placeholder="Mật khẩu..." required />
                                                    <label htmlFor="password" className="form-label">Mật khẩu</label>
                                                </div>
                                            </div>
                                            <div className="col-12">
                                                <div className="d-grid my-3">
                                                    <button className="btn btn-primary btn-lg" type="submit">
                                                        {loadingApi && <i className="fa-solid fa-sync fa-spin loader"></i>}
                                                        Đăng kí
                                                    </button>
                                                </div>
                                            </div>
                                            <div className="col-12">
                                                <p className="m-0 text-secondary text-center">Bạn đã có tài khoản?
                                                    <Link to={'/login'} className="link-primary text-decoration-none"> Đăng nhập</Link>
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
        </>
    );
};

export default SignUp;