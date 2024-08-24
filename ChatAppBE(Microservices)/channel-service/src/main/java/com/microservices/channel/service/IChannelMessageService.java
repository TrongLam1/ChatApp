package com.microservices.channel.service;

import com.microservices.channel.dto.request.MessageRequest;
import com.microservices.channel.dto.response.MessageResponse;

import java.util.List;

public interface IChannelMessageService {

    void sendMessage(MessageRequest request);

    List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId);
}
