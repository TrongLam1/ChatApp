package com.microservices.channel.controller;

import com.microservices.channel.dto.response.ResponseData;
import com.microservices.channel.dto.response.ResponseError;
import com.microservices.channel.service.impl.ChannelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelServiceImpl channelService;

    @GetMapping("/{userId}")
    public ResponseData<?> getChannelByUserId(@PathVariable Long userId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Find channel",
                    channelService.findChannelByUserId(userId));
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
