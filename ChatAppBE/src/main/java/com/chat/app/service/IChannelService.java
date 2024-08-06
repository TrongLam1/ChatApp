package com.chat.app.service;

import com.chat.app.exception.UserException;

public interface IChannelService {
	
	String findChannelByUser(String token, Integer receiver) throws UserException;
}
