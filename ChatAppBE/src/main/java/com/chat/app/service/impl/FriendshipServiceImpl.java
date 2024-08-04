package com.chat.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.app.dto.MessageDTO;
import com.chat.app.exception.UserException;
import com.chat.app.model.Channel;
import com.chat.app.model.Friendship;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;
import com.chat.app.repository.ChannelRepository;
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
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Override
	public FriendshipResponse sendAddFriend(String token, Integer toUser) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			
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
			res.setAvatar(receiver.getImage_url());
			res.setId(receiver.getUserId());
			res.setUserName(receiver.getUserName());
			res.setStatus(StatusFriend.WAITING);
			
			simpMessagingTemplate
			.convertAndSend("/channel/notify/add-friend/" + receiver.getUserId(), "A new friend request from " + sender.getUserName());
			
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public FriendshipResponse acceptAddFriend(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			User receiver = userRepository.findById(userId).get();

			Friendship friendshipReceiver = friendshipRepo.findByUserAndFriend(sender, receiver).get();
			Friendship friendshipSender = friendshipRepo.findByUserAndFriend(receiver, sender).get();
			if (friendshipSender.getStatus().equals(StatusFriend.WAITING)) {
				friendshipReceiver.setStatus(StatusFriend.FRIEND);
				friendshipSender.setStatus(StatusFriend.FRIEND);
				friendshipRepo.save(friendshipSender);
				friendshipRepo.save(friendshipReceiver);
			}
			
			FriendshipResponse res = new FriendshipResponse();
			res.setStatus(StatusFriend.FRIEND);
			res.setUserName(receiver.getUserName());
			res.setId(receiver.getUserId());
			
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	// Cancel add friend from sender request
	@Override
	public FriendshipResponse cancelAddFriend(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			User receiver = userRepository.findById(userId).get();

			Friendship friendshipReceiver = friendshipRepo.findByUserAndFriend(sender, receiver).get();
			Friendship friendshipSender = friendshipRepo.findByUserAndFriend(receiver, sender).get();
			
			friendshipRepo.delete(friendshipSender);
			friendshipRepo.delete(friendshipReceiver);
			
			FriendshipResponse res = new FriendshipResponse();
			res.setAvatar(receiver.getImage_url());
			res.setId(receiver.getUserId());
			res.setUserName(receiver.getUserName());
			res.setStatus(null);
			
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	// Cancel add friend from receiver request
	@Override
	public FriendshipResponse denyAcceptFriend(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			User receiver = userRepository.findById(userId).get();

			Friendship friendshipReceiver = friendshipRepo.findByUserAndFriend(sender, receiver).get();
			Friendship friendshipSender = friendshipRepo.findByUserAndFriend(receiver, sender).get();
			
			friendshipRepo.delete(friendshipSender);
			friendshipRepo.delete(friendshipReceiver);
			
			FriendshipResponse res = new FriendshipResponse();
			res.setAvatar(receiver.getImage_url());
			res.setId(receiver.getUserId());
			res.setUserName(receiver.getUserName());
			res.setStatus(null);
			
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> listFriendsByUser(String token) {
		try {
			String email = jwtService.extractUsername(token);
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new UserException("Not found user " + email));
	        
	        List<User> listFriends = friendshipRepo.getListFriendsByUserAndStatus(user, StatusFriend.FRIEND);
	        
	        return listFriends.stream().map(item -> {
	            FriendshipResponse res = new FriendshipResponse();
	            Channel channel = channelRepository.findByReceiverAndSender(item.getAccount().getUser(), user).orElse(null);

	            if (channel != null && channel.getLastMessage() != null) {
	                MessageDTO lastMessageDTO = MessageDTO.builder()
	                        .sender(channel.getLastMessage().getSender().getUserName())
	                        .content(channel.getLastMessage().getContent())
	                        .image_url(channel.getLastMessage().getImage_url())
	                        .createAt(channel.getLastMessage().getCreateAt().toString())
	                        .build();
	                res.setLastMessage(lastMessageDTO);
	            }

	            res.setId(item.getUserId());
	            res.setStatus(StatusFriend.FRIEND);
	            res.setUserName(item.getUserName());
	            res.setAvatar(item.getImage_url());

	            return res;
	        }).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> listUsersWaitingAccept(String token) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			List<User> listFriends = friendshipRepo.getListUsersByFriendAndStatus(user, StatusFriend.WAITING);

			return listFriends.stream().map(item -> {
				FriendshipResponse res = new FriendshipResponse();
				res.setId(item.getUserId());
				res.setStatus(StatusFriend.WAITING);
				res.setUserName(item.getUserName());
				if (item.getImage_url() != null) res.setAvatar(item.getImage_url());
				return res;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String blockedUser(String token, Integer userId) {
		try {
			String email = jwtService.extractUsername(token);
			User sender = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
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

	@Override
	public int countRequestsAddFriend(String token) throws UserException {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			return friendshipRepo.countByUserAndStatus(user, StatusFriend.WAITING);
		} catch (UserException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<FriendshipResponse> findFriendInListFriends(String token, String username) throws UserException {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			Set<User> listFriends = friendshipRepo.getListFriendsByUserAndStatus(user, StatusFriend.FRIEND)
					.stream().collect(Collectors.toSet());
			return listFriends.stream().filter(friend -> friend.getUserName().equals(username))
					.map(friend -> {
						FriendshipResponse res = new FriendshipResponse();
						res.setStatus(StatusFriend.FRIEND);
						res.setUserName(friend.getUserName());
						res.setId(friend.getUserId());
						
						return res;
					}).collect(Collectors.toList());
		} catch (UserException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
