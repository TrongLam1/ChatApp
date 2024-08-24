package com.microservices.group.service;

import com.microservices.group.dto.request.MessageRequest;
import com.microservices.group.dto.response.MessageResponse;

import java.util.List;

public interface IGroupMessageService {

    void sendMessage(MessageRequest request);

    List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId);
}
