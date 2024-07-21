import axios from './customizeAxios';

const baseUrl = '/api/v1/authentication';

const signUp = (username, email, password) => {
    return axios.post(`${baseUrl}/sign-up`, { username, email, password });
};

const signIn = (email, password) => {
    return axios.post(`${baseUrl}/sign-in`, { email, password });
};

export { signIn, signUp };