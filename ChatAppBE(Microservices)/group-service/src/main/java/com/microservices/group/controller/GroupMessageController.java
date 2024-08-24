package com.microservices.group.controller;

import com.microservices.group.dto.request.MessageRequest;
import com.microservices.group.dto.response.MessageResponse;
import com.microservices.group.dto.response.ResponseData;
import com.microservices.group.service.impl.GroupMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group/group-message")
@RequiredArgsConstructor
public class GroupMessageController {

    private final GroupMessageServiceImpl groupMessageService;

    @PostMapping
    public ResponseData<String> sendMessage(@RequestBody MessageRequest request) {
        try {
            groupMessageService.sendMessage(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Send message successfully.");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{groupId}")
    public ResponseData<List<MessageResponse>> getMessages(@PathVariable Long groupId) {
        try {
            return new ResponseData<>(HttpStatus.CREATED.value(), "List messages group " + groupId,
                    groupMessageService.getListMessagesFromSubscribe(groupId));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
