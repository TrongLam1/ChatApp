import { Routes, Route } from 'react-router-dom';
import Login from '../components/Login/Login';
import SignUp from '../components/Login/SignUp';
import HomePage from '../components/HomePage/HomePage';

const AppRoutes = (props) => {
    return (
        <>
            <Routes>
                <Route path='/login' element={<Login />}></Route>
                <Route path='/register' element={<SignUp />}></Route>
                <Route path='/' element={<HomePage />}></Route>
            </Routes>
        </>
    )
};

export default AppRoutes;