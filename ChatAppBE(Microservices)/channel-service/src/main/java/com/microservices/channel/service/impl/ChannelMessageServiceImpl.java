package com.microservices.channel.service.impl;

import com.microservices.channel.dto.request.MessageRequest;
import com.microservices.channel.dto.response.MessageResponse;
import com.microservices.channel.dto.response.ProfileResponse;
import com.microservices.channel.dto.response.ResponseData;
import com.microservices.channel.entity.Channel;
import com.microservices.channel.entity.ChannelMessage;
import com.microservices.channel.exception.ChannelException;
import com.microservices.channel.repository.ChannelMessageRepository;
import com.microservices.channel.repository.ChannelRepository;
import com.microservices.channel.repository.httpClient.ProfileClient;
import com.microservices.channel.repository.httpClient.SocketClient;
import com.microservices.channel.service.IChannelMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChannelMessageServiceImpl implements IChannelMessageService {

    private final ChannelRepository channelRepository;

    private final ChannelMessageRepository channelMessageRepository;

    private final ProfileClient profileClient;

    private final SocketClient socketClient;

    @Override
    public void sendMessage(MessageRequest request) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Channel channel = channelRepository.findById(request.getChannelId())
                    .orElseThrow(() -> new ChannelException("Not found channel"));
            ChannelMessage message = ChannelMessage.builder()
                    .senderId(userId)
                    .createdDate(LocalDateTime.now())
                    .content(request.getContent())
                    .image_id(request.getImage_id())
                    .image_url(request.getImage_url())
                    .channel(channel)
                    .build();

            channelMessageRepository.save(message);

            ProfileResponse profile = profileClient.getProfileById(message.getSenderId()).getData();

            ResponseData<String> mes = socketClient
                    .sendMessage(MessageResponse.builder()
                            .subscribeId(channel.getId())
                            .sender(profile.getUsername())
                            .content(message.getContent())
                            .createdDate(message.getCreatedDate())
                            .build());

            log.info("Message: {}", mes.getMessage());

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId) {
        try {
            Channel channel = channelRepository.findById(subscribeId)
                    .orElseThrow(() -> new ChannelException("Not found channel"));

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            ProfileResponse myProfile = profileClient.getProfileById(userId).getData();
            ProfileResponse friendProfile;
            if (channel.getUserId().equals(userId)) {
                friendProfile = profileClient.getProfileById(channel.getFriendId()).getData();
            } else {
                friendProfile = profileClient.getProfileById(channel.getUserId()).getData();
            }

            if (channel.getUserId().equals(userId) || channel.getFriendId().equals(userId)) {
                return channelMessageRepository.getAllMessages(channel)
                        .stream().map(message -> {

                            MessageResponse messageResponse = MessageResponse.builder()
                                    .content(message.getContent())
                                    .createdDate(message.getCreatedDate())
                                    .build();

                            // Set sender's name for message from senderId
                            if (message.getSenderId().equals(myProfile.getUserId())) {
                                messageResponse.setSender(myProfile.getUsername());
                            } else {
                                messageResponse.setSender(friendProfile.getUsername());
                            }

                            return messageResponse;
                        }).toList();
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
