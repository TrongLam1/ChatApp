import axios from './customizeAxios';

const baseUrl = '/api/v1/users';

const findUserByEmail = (email) => {
    return axios.get(`${baseUrl}/find-by-email/${email}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const findUserById = (userId) => {
    return axios.get(`${baseUrl}/find-by-id/${userId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const findUsersByName = (name) => {
    return axios.get(`${baseUrl}/find-by-username/${name}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

export { findUserByEmail, findUserById, findUsersByName };