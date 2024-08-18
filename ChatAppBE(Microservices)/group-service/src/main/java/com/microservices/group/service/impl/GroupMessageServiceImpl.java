package com.microservices.group.service.impl;

import com.microservices.group.dto.request.MessageRequest;
import com.microservices.group.dto.response.MessageResponse;
import com.microservices.group.repository.GroupMessageRepository;
import com.microservices.group.repository.GroupRepository;
import com.microservices.group.service.IGroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMessageServiceImpl implements IGroupMessageService {

    private final GroupRepository groupRepository;

    private final GroupMessageRepository groupMessageRepository;

    @Override
    public void sendMessage(MessageRequest request) {

    }

    @Override
    public String sendImage(MessageRequest message, MultipartFile file) {
        return "";
    }

    @Override
    public List<MessageResponse> getListMessagesFromSubscribe(Long subscribeId) {
        return List.of();
    }
}
