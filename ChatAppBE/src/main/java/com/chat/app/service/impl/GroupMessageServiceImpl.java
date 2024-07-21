package com.chat.app.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.app.dto.GroupMessagesDTO;
import com.chat.app.exception.UserException;
import com.chat.app.modal.Group;
import com.chat.app.modal.GroupMessages;
import com.chat.app.modal.User;
import com.chat.app.repository.GroupMemberRepository;
import com.chat.app.repository.GroupMessageRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.MessageRequest;
import com.chat.app.service.IMessageService;

@Service
@Transactional
public class GroupMessageServiceImpl implements IMessageService<GroupMessagesDTO> {

	@Autowired
	private GroupMessageRepository groupMessageRepository;
	
	@Autowired
	private GroupServiceImpl groupService;
	
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private String generateCustomId() {
		long count = groupMessageRepository.count();
		return count < 10 ? "message0" + count : "message" + count;
	}

	@Override
	public void sendMessage(String token, MessageRequest request) {
		try {
			String emailSender = jwtService.extractUsername(token);
			User sender = userRepo.findByEmail(emailSender);
			
			Group group = groupService.findById(request.getSubscribe());
			
			LocalDateTime localDateTime = LocalDateTime.now();
			
			GroupMessages message = new GroupMessages();
			message.setContent(request.getContent());
			message.setGroup(group);
			message.setGroupMessageId(generateCustomId());
			message.setSender(sender);
			
			groupMessageRepository.save(message);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
	        String formattedDate = localDateTime.format(formatter);
			
			GroupMessagesDTO messageDTO = new GroupMessagesDTO();
			messageDTO.setCreateAt(formattedDate);
			messageDTO.setContent(request.getContent());
			messageDTO.setSender(sender.getUserName());
			
			simpMessagingTemplate.convertAndSend("/channel/private/" + group.getGroupId(), messageDTO);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<GroupMessagesDTO> getListMessagesFromSubscribe(String token, String subscribelId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepo.findByEmail(email);
			Group group = groupService.findById(subscribelId);
			
			groupMemberRepository.findByUserAndGroup(sender, group)
					.orElseThrow(() -> new UserException("Not found user in group"));
			
			List<GroupMessages> listMessages = group.getListMessages();
			List<GroupMessagesDTO> listMessagesDTO = listMessages.stream().map(item -> {
				LocalDateTime localDateTime = item.getCreateAt();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
		        String formattedDate = localDateTime.format(formatter);
		        
		        GroupMessagesDTO messageDTO = new GroupMessagesDTO();
				messageDTO.setCreateAt(formattedDate);
				messageDTO.setContent(item.getContent());
				messageDTO.setSender(item.getSender().getUserName());
				
				return messageDTO;
			}).collect(Collectors.toList());
			
			return listMessagesDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
