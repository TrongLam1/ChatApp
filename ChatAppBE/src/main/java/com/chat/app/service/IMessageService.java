package com.chat.app.service;

import java.util.List;

import com.chat.app.request.MessageRequest;

public interface IMessageService <T> {

	void sendMessage(String token, MessageRequest request);
	
	List<T> getListMessagesFromSubscribe(String token, String subscribelId);
}
