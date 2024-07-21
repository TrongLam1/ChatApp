package com.chat.app.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.app.dto.ChannelMessagesDTO;
import com.chat.app.modal.Channel;
import com.chat.app.modal.ChannelMessages;
import com.chat.app.modal.User;
import com.chat.app.repository.ChannelMessageRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.MessageRequest;
import com.chat.app.service.IMessageService;

@Service
@Transactional
public class ChannelMessageServiceImpl implements IMessageService<ChannelMessagesDTO> {

	@Autowired
	private ChannelMessageRepository channelMessageRepo;
	
	@Autowired
	private ChannelServiceImpl channelService;
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private String generateCustomId() {
		long count = channelMessageRepo.count();
		return count < 10 ? "message0" + count : "message" + count;
	}

	@Override
	public void sendMessage(String token, MessageRequest request) {
		try {
			String emailSender = jwtService.extractUsername(token);
			User sender = userRepo.findByEmail(emailSender);
			
			Channel channel = channelService.findChannelById(request.getSubscribe());
			
			LocalDateTime localDateTime = LocalDateTime.now();
			
			ChannelMessages message = new ChannelMessages();
			message.setChannnelMessageId(generateCustomId());
			message.setChannel(channel);
			message.setContent(request.getContent());
			message.setSender(sender);
			message.setCreateAt(localDateTime);
			channelMessageRepo.save(message);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
	        String formattedDate = localDateTime.format(formatter);
			
			ChannelMessagesDTO messageDTO = new ChannelMessagesDTO();
			messageDTO.setContent(message.getContent());
			messageDTO.setSender(sender.getUserName());
			messageDTO.setCreateAt(formattedDate);
			
			simpMessagingTemplate.convertAndSend("/channel/private/" + channel.getChannelId(), messageDTO);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<ChannelMessagesDTO> getListMessagesFromSubscribe(String token, String channelId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepo.findByEmail(email);
			Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "channelMessageId"));
			Channel channel = channelService.findChannelById(channelId);
			
			List<ChannelMessagesDTO> listMessagesDTO = new ArrayList<>();
			
			if (channel.getReceiver().equals(user) || channel.getSender().equals(user)) {
				List<ChannelMessages> listMessages = channel.getListMessages();
				// List<ChannelMessages> listMessages = channelMessageRepo.findByChannel(channel, pageable);
				listMessagesDTO = listMessages.stream().map(item -> {
					LocalDateTime localDateTime = item.getCreateAt();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
			        String formattedDate = localDateTime.format(formatter);
					
					ChannelMessagesDTO messageDTO = new ChannelMessagesDTO();
					messageDTO.setContent(item.getContent());
					messageDTO.setSender(item.getSender().getUserName());
					messageDTO.setCreateAt(formattedDate);
					
					return messageDTO;
				}).collect(Collectors.toList());
			}
			
			return listMessagesDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
