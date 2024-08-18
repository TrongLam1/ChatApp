package com.microservices.channel.service;

import com.microservices.channel.dto.request.MessageRequest;
import com.microservices.channel.dto.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IChannelMessageService {

    void sendMessage(MessageRequest request);

    String sendImage(MessageRequest message, MultipartFile file);

    List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId);
}
