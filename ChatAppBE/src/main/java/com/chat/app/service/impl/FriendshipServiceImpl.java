package com.chat.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.app.modal.Friendship;
import com.chat.app.modal.User;
import com.chat.app.modal.enums.StatusFriend;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.response.FriendshipResponse;
import com.chat.app.service.IFriendshipService;

@Service
@Transactional
public class FriendshipServiceImpl implements IFriendshipService {

	@Autowired
	private FriendshipRepository friendshipRepo;

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public FriendshipResponse sendAddFriend(String token, Integer toUser) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email);
			
			if (sender.getUserId().equals(toUser)) {
				return null;
			}
			
			User receiver = userRepository.findById(toUser).get();
			
			Optional<Friendship> friendship = friendshipRepo.findByUserAndFriend(sender, receiver);
			if (friendship.isEmpty()) {
				Friendship newFriendship = new Friendship();
				newFriendship.setFriend(receiver);
				newFriendship.setUser(sender);
				newFriendship.setStatus(StatusFriend.WAITING);
				friendshipRepo.save(newFriendship);
				
				Friendship newFriendship1 = new Friendship();
				newFriendship1.setFriend(sender);
				newFriendship1.setUser(receiver);
				newFriendship1.setStatus(StatusFriend.PENDING);
				friendshipRepo.save(newFriendship1);
			}
			
			FriendshipResponse res = new FriendshipResponse();
			res.setEmail(receiver.getEmail());
			res.setStatus(StatusFriend.WAITING);
			res.setUserName(receiver.getUserName());
			res.setId(receiver.getUserId());
			
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String acceptAddFriend(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email);
			User receiver = userRepository.findById(userId).get();

			Friendship friendshipReceiver = friendshipRepo.findByUserAndFriend(sender, receiver).get();
			Friendship friendshipSender = friendshipRepo.findByUserAndFriend(receiver, sender).get();
			if (friendshipSender.getStatus().equals(StatusFriend.WAITING)) {
				friendshipReceiver.setStatus(StatusFriend.FRIEND);
				friendshipSender.setStatus(StatusFriend.FRIEND);
				friendshipRepo.save(friendshipSender);
				friendshipRepo.save(friendshipReceiver);
			}
			
			return "Accept friend id " + userId + " success";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	@Override
	public String denyAcceptFriend(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email);
			User receiver = userRepository.findById(userId).get();

			Friendship friendshipReceiver = friendshipRepo.findByUserAndFriend(sender, receiver).get();
			Friendship friendshipSender = friendshipRepo.findByUserAndFriend(receiver, sender).get();
			
			friendshipRepo.delete(friendshipSender);
			friendshipRepo.delete(friendshipReceiver);
			
			return "Deny accept friend id " + userId;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> listFriendsByUser(String token) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email);
			List<User> listFriends = friendshipRepo.getListFriendsByUserAndStatus(user, StatusFriend.FRIEND);
			List<FriendshipResponse> listFriendsDTO = listFriends.stream().map(item -> {
				FriendshipResponse res = new FriendshipResponse();
				res.setId(item.getUserId());
				res.setEmail(item.getEmail());
				res.setStatus(StatusFriend.FRIEND);
				res.setUserName(item.getUserName());
				return res;
			}).collect(Collectors.toList());
			return listFriendsDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> listUsersWaitingAccept(String token) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email);
			List<User> listFriends = friendshipRepo.getListUsersByFriendAndStatus(user, StatusFriend.WAITING);
			List<FriendshipResponse> listFriendsDTO = listFriends.stream().map(item -> {
				FriendshipResponse res = new FriendshipResponse();
				res.setId(item.getUserId());
				res.setEmail(item.getEmail());
				res.setStatus(StatusFriend.WAITING);
				res.setUserName(item.getUserName());
				return res;
			}).collect(Collectors.toList());
			return listFriendsDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String blockedUser(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email);
			User receiver = userRepository.findById(userId).get();

			Friendship friendshipReceiver = friendshipRepo.findByUserAndFriend(sender, receiver).get();
			Friendship friendshipSender = friendshipRepo.findByUserAndFriend(receiver, sender).get();
			
			friendshipReceiver.setStatus(StatusFriend.IS_BLOCKED);
			friendshipSender.setStatus(StatusFriend.BLOCK);
			
			friendshipRepo.save(friendshipSender);
			friendshipRepo.save(friendshipReceiver);
			
			return "Block friend id " + userId + " success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
