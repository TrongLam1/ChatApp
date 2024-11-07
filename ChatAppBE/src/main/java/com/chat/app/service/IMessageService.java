package com.chat.app.service;

import com.chat.app.dto.MessageDTO;
import com.chat.app.request.MessageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMessageService {

    void sendMessage(String token, MessageRequest request);

    String sendImage(String token, MessageRequest message, MultipartFile file);

    List<MessageDTO> getListMessagesFromSubscribe(String token, String subscribelId);
}
