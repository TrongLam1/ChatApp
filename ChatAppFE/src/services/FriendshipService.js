import axios from './customizeAxios';

const basePath = '/api/v1/friendship';

const sendRequestAddFriend = (userId) => {
    return axios.get(`${basePath}/send-add-friend/${userId}`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const acceptAddFriend = (userId) => {
    return axios.get(`${basePath}/add-friend/${userId}`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const denyAcceptFriend = (userId) => {
    return axios.delete(`${basePath}/deny-add-friend/${userId}`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const cancelAcceptFriend = (userId) => {
    return axios.delete(`${basePath}/cancel-add-friend/${userId}`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const fetchListFriends = () => {
    return axios.get(`${basePath}/list-friends`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const fetchListFriendsWaitingAccept = () => {
    return axios.get(`${basePath}/list-friends-waiting-accept`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const countRequestsAddFriend = () => {
    return axios.get(`${basePath}/count-request-add-friend`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

const findFriendByUsername = (username) => {
    return axios.get(`${basePath}/find-friend-by-username/${username}`, {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
};

export {
    sendRequestAddFriend, acceptAddFriend, fetchListFriends, findFriendByUsername,
    fetchListFriendsWaitingAccept, denyAcceptFriend, cancelAcceptFriend, countRequestsAddFriend
};