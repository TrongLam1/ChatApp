package com.chat.app.service;

public interface IChannelService {
	
	String findChannelByUser(String token, Integer receiver);
}
