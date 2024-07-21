import axios from "./customizeAxios";

const API_URL = '/api/v1/chat';

const sendMessage = (message) => {
    return axios.post(`${API_URL}/message`, message);
};

const getMessages = () => {
    return axios.get(`${API_URL}/messages`);
};

export { sendMessage, getMessages };