package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.chat.app.exception.UserException;
import com.chat.app.model.Channel;
import com.chat.app.model.Friendship;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;
import com.chat.app.repository.ChannelRepository;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.service.impl.FriendshipServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

@SpringBootTest
public class FriendshipServiceTests {

	@Autowired
	private FriendshipServiceImpl friendshipService;
	
	@MockBean
	private FriendshipRepository friendshipRepository;
	
	@MockBean
	private JwtServiceImpl jwtService;

	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ChannelRepository channelRepository;
	
	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private String token = "validToken";
	private String senderEmail = "sender@gmail.com";
	private String receiverEmail = "receiver@gmail.com";
	
	private User sender;
	private User receiver;
	private Friendship friendshipSender;
	private Friendship friendshipReceiver;
	private List<User> listFriends = new ArrayList<>();
	private Channel channel;
	
	@BeforeEach
	void init() {
		friendshipSender = Friendship.builder()
				.friendshipId(1).user(sender).friend(receiver).status(StatusFriend.FRIEND).build();
		friendshipReceiver = Friendship.builder()
				.friendshipId(1).user(receiver).friend(sender).status(StatusFriend.FRIEND).build();
		sender = User.builder()
				.userId(1).email(senderEmail).userName("sender").build();
		receiver = User.builder()
				.userId(2).email(receiverEmail).userName("receiver").build();
		channel = Channel.builder()
				.receiver(receiver)
				.sender(sender).build();
	}
	
//	Function sendAddFriend -----------------------------------------------------------
	@Test
	void testSendAddFriend_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.of(receiver));
		when(friendshipRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.empty());
		
		var res = friendshipService.sendAddFriend(token, 2);
		assertThat(res.getStatus().equals(StatusFriend.FRIEND));
		assertThat(res.getUserName().equals(receiver.getUserName()));
	}
	
	@Test
	void testSendAddFriend_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.sendAddFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
	@Test
	void testSendAddFriend_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.sendAddFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found receiver");
	}
	
//	Function acceptAddFriend -----------------------------------------------------------
	@Test
	void testAcceptAddFriend_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.of(receiver));
		when(friendshipRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendshipReceiver));
		when(friendshipRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.of(friendshipSender));
		
		var res = friendshipService.acceptAddFriend(token, 2);
		assertThat(res.getUserName().equals(receiver.getUserName()));
		assertThat(res.getStatus().equals(StatusFriend.FRIEND));
	}
	
	@Test
	void testAcceptAddFriend_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.acceptAddFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
	@Test
	void testAcceptAddFriend_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.acceptAddFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found receiver");
	}
	
//	Function denyAcceptFriend -----------------------------------------------------------
	@Test
	void testDenyAcceptFriend_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.of(receiver));
		when(friendshipRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendshipReceiver));
		when(friendshipRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.of(friendshipSender));
		
		var res = friendshipService.denyAcceptFriend(token, 2);
		assertThat(res.getUserName().equals(receiver.getUserName()));
		assertThat(res.getStatus()).isNull();
	}
	
	@Test
	void testDenyAcceptFriend_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.denyAcceptFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
	@Test
	void testDenyAcceptFriend_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.denyAcceptFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found receiver");
	}
	
//	Function cancelAddFriend -----------------------------------------------------------
	@Test
	void testCancelAddFriend_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.of(receiver));
		when(friendshipRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendshipReceiver));
		when(friendshipRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.of(friendshipSender));
		
		var res = friendshipService.cancelAddFriend(token, 2);
		assertThat(res.getUserName().equals(receiver.getUserName()));
		assertThat(res.getStatus()).isNull();
	}
	
	@Test
	void testCancelAddFriend_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.cancelAddFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
	@Test
	void testCancelAddFriend_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.cancelAddFriend(token, 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found receiver");
	}
	
//	Function findFriendInListFriends -----------------------------------------------------------
	@Test
	void testFindFriendInListFriends_success() throws UserException {
		listFriends.add(receiver);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(friendshipRepository.getListFriendsByUserAndStatus(sender, StatusFriend.FRIEND)).thenReturn(listFriends);
		
		var res = friendshipService.findFriendInListFriends(token, "receiver");
		assertThat(res.size()).isEqualTo(1);
	}
	
	@Test
	void testFindFriendInListFriends_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.findFriendInListFriends(token, senderEmail))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
//	Function countRequestsAddFriend -----------------------------------------------------------
	@Test
	void testCountRequestsAddFriend_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		
		var res = friendshipService.countRequestsAddFriend(token);
		assertThat(res).isEqualTo(0);
	}
	
	@Test
	void testCountRequestsAddFriend_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.countRequestsAddFriend(token))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
//	Function listUsersWaitingAccept -----------------------------------------------------------
	@Test
	void testListUsersWaitingAccept_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(friendshipRepository.getListUsersByFriendAndStatus(sender, StatusFriend.WAITING)).thenReturn(listFriends);
		
		var res = friendshipService.listUsersWaitingAccept(token);
		assertThat(res.size()).isEqualTo(0);
	}
	
	@Test
	void testListUsersWaitingAccept_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.listUsersWaitingAccept(token))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
//	Function listFriendsByUser -----------------------------------------------------------
	@Test
	void testListFriendsByUser_success() throws UserException {
		listFriends.add(receiver);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(friendshipRepository.getListFriendsByUserAndStatus(sender, StatusFriend.FRIEND)).thenReturn(listFriends);
		when(channelRepository.findByReceiverAndSender(receiver, sender)).thenReturn(Optional.of(channel));
		
		var res = friendshipService.listFriendsByUser(token);
		assertThat(res.size()).isEqualTo(1);
	}
	
	@Test
	void testListFriendsByUser_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> friendshipService.listFriendsByUser(token))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
}
