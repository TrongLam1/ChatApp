package com.microservices.channel.service;

import com.microservices.channel.dto.response.ChannelResponse;

public interface IChannelService {

    ChannelResponse findChannelByUserId(Long friendId);
}
