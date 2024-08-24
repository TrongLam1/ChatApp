package com.microservices.socket.controller;

import com.microservices.socket.dto.response.MessageResponse;
import com.microservices.socket.dto.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socket")
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send-message")
    public ResponseData<String> sendMessage(@RequestBody MessageResponse message) {
        try {
            System.out.println(message.getSubscribeId());
            simpMessagingTemplate.convertAndSend("/channel/notify/" + message.getSubscribeId(), message);
            return new ResponseData<>(HttpStatus.OK.value(), "Socket send message");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
