package com.chat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.request.MessageRequest;
import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.GroupMessageServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/group/message")
@Slf4j
public class GroupMessageController {

	@Autowired
	private GroupMessageServiceImpl groupMessageService;
	
	@PostMapping("/private")
	public ResponseData<?> sendMessageToChannel(@RequestHeader("Authorization") String token,
			@RequestBody MessageRequest message) {
		try {
			String jwtToken = token.substring(7);
			groupMessageService.sendMessage(jwtToken, message);
			return new ResponseData<>(HttpStatus.OK.value(), "Send success");
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@GetMapping("/list-messages/{group}")
	public ResponseData<?> getListMessagesFromChannel(@RequestHeader("Authorization") String token,
			@PathVariable("group") String group) {
		try {
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "List message",
					groupMessageService.getListMessagesFromSubscribe(jwtToken, group));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}