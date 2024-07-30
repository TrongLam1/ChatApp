package com.chat.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chat.app.dto.MessageDTO;
import com.chat.app.request.MessageRequest;

public interface IMessageService {

	void sendMessage(String token, MessageRequest request);
	
	String sendImage(String token, MessageRequest message, MultipartFile file);
	
	List<MessageDTO> getListMessagesFromSubscribe(String token, String subscribelId);
	
	List<MessageDTO> getListMessagesLazyLoad(String token, String subscribelId, int pageNo);
}
