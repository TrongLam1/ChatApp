package com.chat.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chat.app.dto.MessageDTO;
import com.chat.app.dto.UserDTO;
import com.chat.app.exception.UserException;
import com.chat.app.model.Channel;
import com.chat.app.model.Friendship;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;
import com.chat.app.repository.ChannelRepository;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.response.FriendshipResponse;
import com.chat.app.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FriendshipRepository friendshipRepo;
	
	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CloudinaryServiceImpl cloudinaryService;

	@Override
	public FriendshipResponse findByEmail(String email, String token) {
		try {
			String emailSender = jwtService.extractUsername(token);
			FriendshipResponse response = new FriendshipResponse();

			if (emailSender.equalsIgnoreCase(email)) {
				return response;
			}

			User sender = userRepository.findByEmail(emailSender)
					.orElseThrow(() -> new UserException("Not found user " + emailSender));
			User friend = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));

			Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, friend);

			if (friendship.isEmpty()) {
				response.setStatus(null);
			} else {
				response.setStatus(friendship.get().getStatus());
				if (friendship.get().getStatus().equals(StatusFriend.FRIEND)) {
					Channel channel = channelRepository.findByReceiverAndSender(sender, friend).orElse(null);
					if (channel.getLastMessage() != null) {
						MessageDTO lastMessageDTO = MessageDTO.builder()
								.sender(channel.getLastMessage().getSender().getUserName())
								.content(channel.getLastMessage().getContent())
								.image_url(channel.getLastMessage().getImage_url())
								.createAt(channel.getLastMessage().getCreateAt().toString())
								.build();
						response.setLastMessage(lastMessageDTO);
					}
				}
			}

			response.setEmail(email);
			response.setId(friend.getUserId());
			response.setUserName(friend.getUserName());
			if (friend.getImage_url() != null) response.setAvatar(friend.getImage_url());

			return response;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public FriendshipResponse findById(Integer userId, String token) {
		try {
			String emailSender = jwtService.extractUsername(token);
			FriendshipResponse response = new FriendshipResponse();

			User sender = userRepository.findByEmail(emailSender)
					.orElseThrow(() -> new UserException("Not found user " + emailSender));
			User friend = userRepository.findById(userId)
					.orElseThrow(() -> new UserException("Not found user id " + userId));

			Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, friend);

			if (friendship.isEmpty()) {
				response.setStatus(null);
			} else {
				response.setStatus(friendship.get().getStatus());
				if (friendship.get().getStatus().equals(StatusFriend.FRIEND)) {
					Channel channel = channelRepository.findByReceiverAndSender(sender, friend).orElse(null);
					if (channel.getLastMessage() != null) {
						MessageDTO lastMessageDTO = MessageDTO.builder()
								.sender(channel.getLastMessage().getSender().getUserName())
								.content(channel.getLastMessage().getContent())
								.image_url(channel.getLastMessage().getImage_url())
								.createAt(channel.getLastMessage().getCreateAt().toString())
								.build();
						response.setLastMessage(lastMessageDTO);
					}
				}
			}

			response.setEmail(friend.getEmail());
			response.setId(friend.getUserId());
			response.setUserName(friend.getUserName());
			if (friend.getImage_url() != null) response.setAvatar(friend.getImage_url());

			return response;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> findByUsername(String token, String username) {
		try {
			String emailSender = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(emailSender)
					.orElseThrow(() -> new UserException("Not found user " + emailSender));

			List<User> listUsers = userRepository.findByUserNameContaining(username);
			List<FriendshipResponse> response = new ArrayList<FriendshipResponse>();
			if (listUsers.size() == 0) {
				return null;
			} else {
				response = listUsers.stream().filter(item -> !sender.equals(item)).map(item -> {
					Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, item);

					FriendshipResponse friend = new FriendshipResponse();
					friend.setEmail(item.getEmail());
					friend.setId(item.getUserId());
					friend.setUserName(item.getUserName());
					if (item.getImage_url() != null) friend.setAvatar(item.getImage_url());
					if (friendship.isEmpty()) {
						friend.setStatus(null);
					} else {
						friend.setStatus(friendship.get().getStatus());
					}
					return friend;
				}).collect(Collectors.toList());
			}

			return response;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public UserDTO updateUsername(String token, String username) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			user.setUserName(username);
			userRepository.save(user);
			
			return mapper.map(user, UserDTO.class);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public UserDTO changeAvatar(String token, MultipartFile file) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			
			if (user.getImage_id() != null) cloudinaryService.delete(user.getImage_id());
			
			Map result = cloudinaryService.upload(file);
			user.setImage_url((String) result.get("url"));
			user.setImage_id((String) result.get("public_id"));
			userRepository.save(user);
			
			return mapper.map(user, UserDTO.class);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
