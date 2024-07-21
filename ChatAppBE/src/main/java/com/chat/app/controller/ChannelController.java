package com.chat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.ChannelServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
public class ChannelController {

	@Autowired
	private ChannelServiceImpl channelService;

	@GetMapping("/user/{userId}")
	public ResponseData<?> findChannelByUser(@RequestHeader("Authorization") String token,
			@PathVariable("userId") Integer userId) {
		try {
			log.info("Find channel user id {}", userId);
			String jwtToken = token.substring(7);
			String channelId = channelService.findChannelByUser(jwtToken, userId);
			return new ResponseData<>(HttpStatus.OK.value(), "Channel", channelId);
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}
