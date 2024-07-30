import axios from './customizeAxios';

const basePath = '/api/v1/channel';

const findChannelByUser = (userId) => {
    return axios.get(`${basePath}/user/${userId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const sendMessageToChannel = (messageRequest) => {
    return axios.post(`${basePath}/message/private`, messageRequest, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
};

const fetchListMessagesFromChannel = (channel) => {
    return axios.get(`${basePath}/message/list-messages/${channel}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const fetchListMessagesLazy = (channel, page) => {
    return axios.get(`${basePath}/message/list-messages/${channel}/page/${page}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const sendImageToChannel = (messageRequest) => {
    return axios.post(`${basePath}/message/private/image`, messageRequest, {
        headers: {
            'Content-type': 'multipart/form-data',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    });
};

export {
    findChannelByUser, sendMessageToChannel, fetchListMessagesFromChannel,
    sendImageToChannel, fetchListMessagesLazy
};