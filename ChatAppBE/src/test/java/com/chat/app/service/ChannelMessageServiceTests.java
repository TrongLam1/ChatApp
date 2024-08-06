package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.chat.app.dto.MessageDTO;
import com.chat.app.model.Channel;
import com.chat.app.model.ChannelMessages;
import com.chat.app.model.User;
import com.chat.app.repository.ChannelMessageRepository;
import com.chat.app.repository.ChannelRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.MessageRequest;
import com.chat.app.service.impl.ChannelMessageServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

@SpringBootTest
public class ChannelMessageServiceTests {

	@Autowired
	private ChannelMessageServiceImpl channelMessageService;
	
	@MockBean
	private ChannelMessageRepository channelMessageRepo;
	
	@MockBean
	private ChannelRepository channelRepo;
	
	@MockBean
	private JwtServiceImpl jwtService;
	
	@MockBean
	private UserRepository userRepo;
	
	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private final String token = "validToken";
	private final String senderEmail = "sender@gmail.com";
	private final String receiverEmail = "receiver@gmail.com";
	
	private MessageRequest messageReq;
	private ChannelMessages message;
	private List<ChannelMessages> listMessages = new ArrayList<ChannelMessages>();
	private User sender;
	private User receiver;
	private Channel channel;
	private MultipartFile file;
	
	@BeforeEach
	void init() {
		sender = User.builder()
				.userId(1).email(senderEmail).userName("sender").build();
		receiver = User.builder()
				.userId(2).email(receiverEmail).userName("receiver").build();
		channel = Channel.builder()
				.channelId("channel01")
				.receiver(receiver)
				.sender(sender).build();
		messageReq = MessageRequest.builder()
				.subscribe("channel1")
				.content("test")
				.image_url("abc")
				.image_id("abc")
				.build();
		message = ChannelMessages.builder()
				.channnelMessageId("message01")
				.sender(sender)
				.channel(channel)
				.content("test")
				.image_url("abc")
				.image_id("abc")
				.createAt(LocalDateTime.now()).build();
		file = new MockMultipartFile(
	            "avatar",
	            "avatar.png",
	            "image/png",
	            "avatar image content".getBytes()
	        );	
	}
	
//	Function sendMessage ----------------------------------------------------------------
	@Test
	void testSendMessage_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(channelRepo.findById(any())).thenReturn(Optional.of(channel));
		when(channelMessageRepo.save(any(ChannelMessages.class))).thenReturn(message);
		when(channelMessageRepo.count()).thenReturn(1L);
		
		channelMessageService.sendMessage(token, messageReq);
		
		verify(channelMessageRepo).save(any(ChannelMessages.class));
		verify(channelRepo).save(channel);
		verify(simpMessagingTemplate).convertAndSend(eq("/channel/private/channel01"), any(MessageDTO.class));
        verify(simpMessagingTemplate).convertAndSend(eq("/channel/notify/2"), any(MessageDTO.class));
	}
	
//	Function sendImage ------------------------------------------------------------------
	@Test
	void testSendImage_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(channelRepo.findById(any())).thenReturn(Optional.of(channel));
		when(channelMessageRepo.save(any(ChannelMessages.class))).thenReturn(message);
		when(channelMessageRepo.count()).thenReturn(1L);
		
		channelMessageService.sendImage(token, messageReq, file);
		
		verify(channelMessageRepo).save(any(ChannelMessages.class));
		verify(channelRepo).save(channel);
		verify(simpMessagingTemplate).convertAndSend(eq("/channel/private/channel01"), any(MessageDTO.class));
        verify(simpMessagingTemplate).convertAndSend(eq("/channel/notify/2"), any(MessageDTO.class));
	}
	
//	Function getListMessagesFromSubscribe -----------------------------------------------
	@Test
	void testGetListMessagesFromSubscribe_success() {
		listMessages.add(message);
		channel.setListMessages(listMessages);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(channelRepo.findById(any())).thenReturn(Optional.of(channel));
		
		var res = channelMessageService.getListMessagesFromSubscribe(token, receiverEmail);
		assertThat(res.size()).isEqualTo(1);
	}
}
