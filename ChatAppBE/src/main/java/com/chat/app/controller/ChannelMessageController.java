package com.chat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chat.app.request.MessageRequest;
import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.ChannelMessageServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/channel/message")
@Slf4j
public class ChannelMessageController {

	@Autowired
	private ChannelMessageServiceImpl channelMessageService;

	@PostMapping("/private")
	public ResponseData<?> sendMessageToChannel(@RequestHeader("Authorization") String token,
			@RequestBody MessageRequest message) {
		try {
			log.info("Send message to channel {}", message.getSubscribe());
			String jwtToken = token.substring(7);
			channelMessageService.sendMessage(jwtToken, message);
			return new ResponseData<>(HttpStatus.OK.value(), "Send success");
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@GetMapping("/list-messages/{channel}")
	public ResponseData<?> getListMessagesFromChannel(@RequestHeader("Authorization") String token,
			@PathVariable("channel") String channel) {
		try {
			log.info("List messages from channel {}", channel);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "List message",
					channelMessageService.getListMessagesFromSubscribe(jwtToken, channel));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@PostMapping("/private/image")
	public ResponseData<?> uploadImg(@RequestHeader("Authorization") String token,
			@ModelAttribute MessageRequest message,
			@RequestParam(value = "file") MultipartFile file) {
		try {
			log.info("Send image to channel {}", message.getSubscribe());
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Send image success",
					channelMessageService.sendImage(jwtToken, message, file));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}
