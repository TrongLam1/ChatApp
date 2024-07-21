import axios from "axios";
// import { refreshToken, logout } from "./AuthenticationService";

const instance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: { 'Content-Type': 'application/json' }
});

// async function refreshJwtToken() {
//     try {
//         let response = await refreshToken();
//         if (response && response.status === 200) {
//             localStorage.setItem('token', response.data.token);
//             localStorage.setItem('refreshToken', response.data.refreshToken);
//         }
//         return response;
//     } catch (error) {
//         throw error;
//     }
// }

instance.interceptors.response.use(function (response) {
    return response.data ? response.data : { statusCode: response.status };
}, async function (error) {
    let res = {};
    if (error.response) {
        res.data = error.response.data;
        res.status = error.response.status;
        res.headers = error.response.headers;
        // if (res.data && typeof res.data === 'string' && res.data.includes("JWT expired")) {
        //     const responseRefresh = await refreshJwtToken();
        //     console.log(responseRefresh);
        //     if (responseRefresh && responseRefresh.status === 200) {
        //         //window.location.reload();
        //     }
        // }
    } else if (error.request) {
        console.log(error.request);
    } else {
        console.log(error);
    }
    return res;
});

export default instance;