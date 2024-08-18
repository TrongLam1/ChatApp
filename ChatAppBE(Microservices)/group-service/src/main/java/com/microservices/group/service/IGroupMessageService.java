package com.microservices.group.service;

import com.microservices.group.dto.request.MessageRequest;
import com.microservices.group.dto.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IGroupMessageService {

    void sendMessage(MessageRequest request);

    String sendImage(MessageRequest message, MultipartFile file);

    List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId);
}
