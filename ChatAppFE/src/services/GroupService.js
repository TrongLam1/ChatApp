import axios from './customizeAxios';

const baseUrl = '/api/v1/group'

const fetchGroupsFromUser = () => {
    return axios.get(`${baseUrl}/lists-from-user`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const findGroupById = (groupId) => {
    return axios.get(`${baseUrl}/${groupId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const createNewGroup = (request) => {
    return axios.post(`${baseUrl}`, request, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const fetchMembersOfGroup = (groupId) => {
    return axios.get(`${baseUrl}/members/${groupId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

const sendMessageToGroup = (messageRequest) => {
    return axios.post(`${baseUrl}/message/private`, messageRequest, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
};

const fetchListMessagesFromGroup = (groupId) => {
    return axios.get(`${baseUrl}/message/list-messages/${groupId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
};

export {
    fetchGroupsFromUser, fetchMembersOfGroup, createNewGroup, findGroupById,
    sendMessageToGroup, fetchListMessagesFromGroup
};