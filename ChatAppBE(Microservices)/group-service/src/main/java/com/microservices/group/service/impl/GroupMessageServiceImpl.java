package com.microservices.group.service.impl;

import com.microservices.group.dto.request.MessageRequest;
import com.microservices.group.dto.response.MessageResponse;
import com.microservices.group.dto.response.ProfileResponse;
import com.microservices.group.entity.Group;
import com.microservices.group.entity.GroupMember;
import com.microservices.group.entity.GroupMessage;
import com.microservices.group.exception.GroupException;
import com.microservices.group.repository.GroupMessageRepository;
import com.microservices.group.repository.GroupRepository;
import com.microservices.group.repository.httpClient.ProfileClient;
import com.microservices.group.repository.httpClient.SocketClient;
import com.microservices.group.service.IGroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMessageServiceImpl implements IGroupMessageService {

    private final GroupRepository groupRepository;

    private final GroupMessageRepository groupMessageRepository;

    private final ProfileClient profileClient;

    private final SocketClient socketClient;

    @Override
    public void sendMessage(MessageRequest request) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new GroupException("Not found group"));
            GroupMessage message = GroupMessage.builder()
                    .senderId(userId)
                    .createdDate(LocalDateTime.now())
                    .content(request.getContent())
                    .group(group)
                    .build();

            List<GroupMember> membersGroup = group.getGroupMembers();
            Set<Long> membersIdGroup = membersGroup.stream().map(GroupMember::getUserId).collect(Collectors.toSet());

            ProfileResponse profileSender = profileClient.getProfileById(userId).getData();

            // Send notification for members in group except sender
            membersIdGroup.stream().filter(memberId -> !memberId.equals(message.getSenderId()))
                    .forEach(memberId -> socketClient.sendMessage(MessageResponse.builder()
                            .subscribeId(group.getId())
                            .createdDate(message.getCreatedDate())
                            .sender(profileSender.getUsername())
                            .content(message.getContent())
                            .build()));

            groupMessageRepository.save(message);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId) {
        try {
            Group group = groupRepository.findById(subscribeId)
                    .orElseThrow(() -> new GroupException("Not found group"));

            // Get member's id to set
            List<GroupMember> membersGroup = group.getGroupMembers();
            Set<Long> membersIdGroup = membersGroup.stream().map(GroupMember::getUserId).collect(Collectors.toSet());

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            // Put userId and username to hashmap
            HashMap<Long, String> profileMembers = new HashMap<>();
            ProfileResponse myProfile = profileClient.getProfileById(userId).getData();
            profileMembers.put(myProfile.getUserId(), myProfile.getUsername());

            for (Long memberId : membersIdGroup) {
                // Get profile member from memberId and put to hashmap
                ProfileResponse memberProfile = profileClient.getProfileById(memberId).getData();
                profileMembers.put(memberProfile.getUserId(), memberProfile.getUsername());
            }

            List<GroupMessage> messagesGroup = group.getGroupMessages();
            List<MessageResponse> messageResponseList = new ArrayList<>();

            // Convert to message response and set sender's name for message from hashmap
            for (GroupMessage message : messagesGroup) {
                MessageResponse messageResponse = MessageResponse.builder()
                        .content(message.getContent())
                        .createdDate(message.getCreatedDate())
                        .sender(profileMembers.get(message.getSenderId()))
                        .build();
                messageResponseList.add(messageResponse);
            }

            return messageResponseList;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
