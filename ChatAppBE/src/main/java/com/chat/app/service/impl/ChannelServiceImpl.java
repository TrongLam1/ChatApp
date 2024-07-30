package com.chat.app.service.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.app.exception.ChannelException;
import com.chat.app.model.Channel;
import com.chat.app.model.User;
import com.chat.app.repository.ChannelRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.service.IChannelService;

@Service
@Transactional
public class ChannelServiceImpl implements IChannelService {

	@Autowired
	private ChannelRepository channelRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtServiceImpl jwtService;

	private String generateCustomId() {
		long count = channelRepo.count();
		return count < 10 ? "channel0" + (count + 1) : "channel" + (count + 1);
	}

	private String createNewChannel(User receiver, User sender) {
		if (!sender.getUserId().equals(receiver.getUserId())) {
			String customId = generateCustomId();
			Channel newChannel = new Channel();
			newChannel.setChannelId(customId);
			newChannel.setReceiver(receiver);
			newChannel.setSender(sender);
			newChannel.setListMessages(new ArrayList<>());
			channelRepo.save(newChannel);

			return newChannel.getChannelId();
		}
		return null;
	}

	@Override
	public String findChannelByUser(String token, Integer receiverId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepo.findByEmail(email);
			User receiver = userRepo.findById(receiverId).get();
			Optional<Channel> channel = channelRepo.findByReceiverAndSender(sender, receiver);

			String channelId = "";
			if (channel.isEmpty()) {
				channelId = createNewChannel(sender, receiver);
			} else {
				channelId = channel.get().getChannelId();
			}
			
			if (channelId == null) {
				throw new RuntimeException("Not found channel");
			}

			return channelId;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public Channel findChannelById(String channelId) {
		try {
			Channel channel = channelRepo.findById(channelId)
					.orElseThrow(() -> new ChannelException("Not found channel " + channelId));
			return channel;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

}
