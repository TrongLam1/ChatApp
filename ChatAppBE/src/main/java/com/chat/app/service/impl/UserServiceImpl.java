package com.chat.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.app.dto.UserDTO;
import com.chat.app.exception.UserException;
import com.chat.app.modal.Friendship;
import com.chat.app.modal.User;
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
	private JwtServiceImpl jwtService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public FriendshipResponse findByEmail(String email, String token) {
		try {
			String emailSender = jwtService.extractUsername(token);
			FriendshipResponse response = new FriendshipResponse();

			if (emailSender.equalsIgnoreCase(email)) {
				return response;
			}

			User sender = userRepository.findByEmail(emailSender);
			User friend = userRepository.findByEmail(email);

			if (sender == null || friend == null) {
				throw new RuntimeException("Not found user.");
			}

			Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, friend);

			if (friendship.isEmpty()) {
				response.setStatus(null);
			} else {
				response.setStatus(friendship.get().getStatus());
			}

			response.setEmail(email);
			response.setId(friend.getUserId());
			response.setUserName(friend.getUserName());

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

			User sender = userRepository.findByEmail(emailSender);
			User friend = userRepository.findById(userId).get();

			if (sender == null || friend == null) {
				throw new UserException("Not found user.");
			}

			Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, friend);

			if (friendship.isEmpty()) {
				response.setStatus(null);
			} else {
				response.setStatus(friendship.get().getStatus());
			}

			response.setEmail(friend.getEmail());
			response.setId(friend.getUserId());
			response.setUserName(friend.getUserName());

			return response;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public UserDTO findById(Integer userId) {
		try {
			User user = userRepository.findById(userId).get();
			if (user == null) {
				throw new RuntimeException("Not found user " + userId);
			}
			return mapper.map(user, UserDTO.class);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> findByUsername(String token, String username) {
		try {
			String emailSender = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(emailSender);

			List<User> listUsers = userRepository.findByUserNameContaining(username);
			List<FriendshipResponse> response = new ArrayList<FriendshipResponse>();
			if (listUsers.size() == 0) {
				return null;
			} else {
				response = listUsers.stream()
						.filter(item -> !sender.equals(item)).map(item -> {
					if (sender.equals(item))
						return null;
					Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, item);

					FriendshipResponse friend = new FriendshipResponse();
					friend.setEmail(item.getEmail());
					friend.setId(item.getUserId());
					friend.setUserName(item.getUserName());
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
}
