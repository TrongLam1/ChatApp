import './Login.scss';
import logo from '../../assets/image/logo.png';
import { useNavigate, Link } from 'react-router-dom';
import { useContext, useState } from "react";
import { toast } from "react-toastify";
import { UserContext } from '../../context/UserContext';
import { WebSocketContext } from '../../context/WebSocketContext';
import { signIn } from '../../services/AuthenticationService';

const Login = (props) => {

    const navigate = useNavigate();
    const { loginContext } = useContext(UserContext);
    const { setChannelNotify } = useContext(WebSocketContext);

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loadingApi, setLoadingApi] = useState(false);

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

        let res = await signIn(email, password);
        if (res && res.status === 200) {
            loginContext(res.data.name, res.data.token, res.data.avatar);
            setChannelNotify(res.data.userId);
            localStorage.setItem('refreshToken', res.data.refreshToken);
            navigate("/");
        } else {
            if (res && res.status === 400) {
                let index = res.message.lastIndexOf(":");
                let errorMessage = res.message.substring(+index + 1);
                toast.error(errorMessage);
            }
        }

        setLoadingApi(false);
    };

    const validateEmail = (email) => {
        // Regular expression for validating an email
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
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
                                            <img src={logo} alt="Logo" width="80" height="80" />
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
                                                    <Link to={'/register'} className="link-primary text-decoration-none"> Sign Up</Link>
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

export default Login;