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
import com.chat.app.model.Group;
import com.chat.app.model.GroupMember;
import com.chat.app.model.GroupMessages;
import com.chat.app.model.User;
import com.chat.app.repository.GroupMemberRepository;
import com.chat.app.repository.GroupMessageRepository;
import com.chat.app.repository.GroupRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.MessageRequest;
import com.chat.app.service.impl.GroupMessageServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

@SpringBootTest
public class GroupMessageServiceTests {

	@Autowired
	private GroupMessageServiceImpl groupMessageService;

	@MockBean
	private GroupMessageRepository groupMessageRepository;

	@MockBean
	private GroupRepository groupRepo;

	@MockBean
	private GroupMemberRepository groupMemberRepository;

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
	private GroupMessages message;
	private User sender;
	private User receiver;
	private Group group;
	private GroupMember admin;
	private GroupMember member;
	private List<GroupMessages> listMessages = new ArrayList<GroupMessages>();
	private List<GroupMember> listMembers = new ArrayList<>();
	
	@BeforeEach
	void init() {
		sender = User.builder()
				.userId(1).email(senderEmail).userName("sender").build();
		receiver = User.builder()
				.userId(2).email(receiverEmail).userName("receiver").build();
		admin = GroupMember.builder()
				.group(group).user(sender).build();
		member = GroupMember.builder()
				.group(group).user(receiver).build();
		listMembers.add(admin);
		group = Group.builder()
				.groupId("group01")
				.listMembers(listMembers)
				.creator(sender).build();
		messageReq = MessageRequest.builder()
				.subscribe("channel1")
				.content("test")
				.image_url("abc")
				.image_id("abc")
				.build();
		message = GroupMessages.builder()
				.groupMessageId("message01")
				.sender(sender)
				.group(group)
				.content("test")
				.image_url("abc")
				.image_id("abc")
				.createAt(LocalDateTime.now()).build();
	}
	
//	Function sendMessage ----------------------------------------------------------------
	@Test
	void testSendMessage_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepo.findById(any())).thenReturn(Optional.of(group));
		when(groupMessageRepository.save(any(GroupMessages.class))).thenReturn(message);
		when(groupMemberRepository.count()).thenReturn(1L);
		
		groupMessageService.sendMessage(token, messageReq);
		
		verify(groupMessageRepository).save(any(GroupMessages.class));
		verify(groupRepo).save(group);
		verify(simpMessagingTemplate).convertAndSend(eq("/channel/private/group01"), any(MessageDTO.class));
	}
	
//	Function sendImage ------------------------------------------------------------------
	@Test
	void testSendImage_success() {
		MultipartFile file = new MockMultipartFile(
	            "avatar",
	            "avatar.png",
	            "image/png",
	            "avatar image content".getBytes()
	        );
				
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepo.findById(any())).thenReturn(Optional.of(group));
		when(groupMessageRepository.save(any(GroupMessages.class))).thenReturn(message);
		when(groupMemberRepository.findByUserAndGroup(sender, group)).thenReturn(Optional.of(admin));
		when(groupMemberRepository.count()).thenReturn(1L);
		
		groupMessageService.sendImage(token, messageReq, file);
		
		verify(groupMessageRepository).save(any(GroupMessages.class));
		verify(groupRepo).save(group);
		verify(simpMessagingTemplate).convertAndSend(eq("/channel/private/group01"), any(MessageDTO.class));
	}
	
//	Function getListMessagesFromSubscribe -----------------------------------------------
	@Test
	void testGetListMessagesFromSubscribe_success() {
		listMessages.add(message);
		group.setListMessages(listMessages);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepo.findById(any())).thenReturn(Optional.of(group));
		when(groupMemberRepository.findByUserAndGroup(sender, group)).thenReturn(Optional.of(admin));
		
		var res = groupMessageService.getListMessagesFromSubscribe(token, senderEmail);
		assertThat(res.size()).isEqualTo(1);
	}
}
