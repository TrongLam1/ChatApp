package com.chat.app.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.chat.app.dto.MessageDTO;
import com.chat.app.model.Channel;
import com.chat.app.model.ChannelMessages;
import com.chat.app.model.User;
import com.chat.app.repository.ChannelMessageRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.MessageRequest;
import com.chat.app.service.IMessageService;

@Service
@Transactional
public class ChannelMessageServiceImpl implements IMessageService {

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
	
	@Autowired
	private CloudinaryServiceImpl cloudinaryService;
	
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
			message.setSender(sender);
			message.setCreateAt(localDateTime);
			message.setContent(request.getContent());
			message.setImage_url(request.getImage_url());
			message.setImage_id(request.getImage_id());
			
			channelMessageRepo.save(message);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
	        String formattedDate = localDateTime.format(formatter);
			
	        MessageDTO messageDTO = new MessageDTO();
			messageDTO.setContent(message.getContent());
			messageDTO.setSender(sender.getUserName());
			messageDTO.setCreateAt(formattedDate);
			messageDTO.setImage_url(message.getImage_url());
			
			simpMessagingTemplate.convertAndSend("/channel/private/" + channel.getChannelId(), messageDTO);
			
			if (sender.equals(channel.getReceiver())) {
				simpMessagingTemplate.convertAndSend("/channel/notify/" + channel.getSender().getUserId(), messageDTO);
			} else {
				simpMessagingTemplate.convertAndSend("/channel/notify/" + channel.getReceiver().getUserId(), messageDTO);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<MessageDTO> getListMessagesFromSubscribe(String token, String channelId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepo.findByEmail(email);
			Channel channel = channelService.findChannelById(channelId);
			
			List<MessageDTO> listMessagesDTO = new ArrayList<>();
			
			if (channel.getReceiver().equals(user) || channel.getSender().equals(user)) {
				List<ChannelMessages> listMessages = channel.getListMessages();
				listMessagesDTO = listMessages.stream().map(item -> {
					LocalDateTime localDateTime = item.getCreateAt();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
			        String formattedDate = localDateTime.format(formatter);
					
			        MessageDTO messageDTO = new MessageDTO();
					messageDTO.setContent(item.getContent());
					messageDTO.setSender(item.getSender().getUserName());
					messageDTO.setCreateAt(formattedDate);
					messageDTO.setImage_url(item.getImage_url());
					
					return messageDTO;
				}).collect(Collectors.toList());
			}
			
			return listMessagesDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String sendImage(String token, MessageRequest message, MultipartFile file) {
		try {
			Map result = cloudinaryService.upload(file);
			message.setImage_url((String) result.get("url"));
			message.setImage_id((String) result.get("public_id"));
			sendMessage(token, message);
			return "Send img success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<MessageDTO> getListMessagesLazyLoad(String token, String channelId, int pageNo) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepo.findByEmail(email);
			Channel channel = channelService.findChannelById(channelId);
			
			List<MessageDTO> listMessagesDTO = new ArrayList<>();
			
			Sort.Direction direction = Sort.Direction.DESC;
	        Sort sort = Sort.by(direction, "channnelMessageId");
	        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
			
			if (channel.getReceiver().equals(user) || channel.getSender().equals(user)) {
				List<ChannelMessages> listMessages = channelMessageRepo.findByChannel(channel, pageable);
				listMessagesDTO = listMessages.stream().map(item -> {
					LocalDateTime localDateTime = item.getCreateAt();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
			        String formattedDate = localDateTime.format(formatter);
					
			        MessageDTO messageDTO = new MessageDTO();
					messageDTO.setContent(item.getContent());
					messageDTO.setSender(item.getSender().getUserName());
					messageDTO.setCreateAt(formattedDate);
					messageDTO.setImage_url(item.getImage_url());
					
					return messageDTO;
				}).collect(Collectors.toList());
			}
			
			return listMessagesDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
