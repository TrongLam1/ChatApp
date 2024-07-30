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

const changeAvatarUser = (file) => {
    return axios.post(`${baseUrl}/change-avatar`, file, {
        headers: {
            'Content-type': 'multipart/form-data',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    });
};

const updateUsername = (username) => {
    return axios.put(`${baseUrl}/update-username?username=${username}`, {}, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

export {
    findUserByEmail, findUserById, findUsersByName,
    changeAvatarUser, updateUsername
};