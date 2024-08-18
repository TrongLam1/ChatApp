package com.microservices.channel.controller;

import com.microservices.channel.dto.request.MessageRequest;
import com.microservices.channel.dto.response.ResponseData;
import com.microservices.channel.dto.response.ResponseError;
import com.microservices.channel.service.impl.ChannelMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel/channel-message")
@RequiredArgsConstructor
public class ChannelMessageController {

    private final ChannelMessageServiceImpl channelMessageService;

    @PostMapping
    public ResponseData<?> sendMessage(@RequestBody MessageRequest request) {
        try {
            channelMessageService.sendMessage(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Send message");
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list-messages/{channelId}")
    public ResponseData<?> getListMessages(@PathVariable Long channelId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List messages",
                    channelMessageService.getListMessagesFromSubscribe(channelId));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
