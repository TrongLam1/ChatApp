package com.microservices.channel.service.impl;

import com.microservices.channel.dto.response.ChannelResponse;
import com.microservices.channel.entity.Channel;
import com.microservices.channel.exception.ChannelException;
import com.microservices.channel.repository.ChannelRepository;
import com.microservices.channel.repository.httpClient.FriendshipClient;
import com.microservices.channel.service.IChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelServiceImpl implements IChannelService {

    private final ChannelRepository channelRepository;

    private final FriendshipClient friendshipClient;

    @Override
    public ChannelResponse findChannelByUserId(Long friendId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            if (!friendshipClient.checkStatusFriend(friendId).getData())
                throw new ChannelException("You are not friend.");

            Optional<Channel> channel = channelRepository.findByUserIdAndFriendId(userId, friendId);
            if (channel.isEmpty()) {
                Channel newChannel = Channel.builder()
                        .userId(userId)
                        .friendId(friendId)
                        .listMessages(new ArrayList<>())
                        .build();

                newChannel = channelRepository.save(newChannel);

                return ChannelResponse.builder().channelId(newChannel.getId()).build();
            } else {
                return ChannelResponse.builder().channelId(channel.get().getId()).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
