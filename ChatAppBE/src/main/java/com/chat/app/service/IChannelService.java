package com.chat.app.service;

import com.chat.app.modal.Channel;

public interface IChannelService {
	
	String findChannelByUser(String token, Integer receiver);
	
	Channel findChannelById(String channelId);
}
