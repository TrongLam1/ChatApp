package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.chat.app.model.Friendship;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.response.FriendshipResponse;
import com.chat.app.service.impl.CloudinaryServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;
import com.chat.app.service.impl.UserServiceImpl;

@SpringBootTest
public class UserServiceTests {

	@Autowired
	private UserServiceImpl userService;
	
	@MockBean
	private UserRepository userRepo;
	
	@MockBean
	private FriendshipRepository friendshipRepo;
	
	@MockBean
	private JwtServiceImpl jwtService;
	
	@MockBean
	private CloudinaryServiceImpl cloudinaryService;
	
	private String token = "validToken";
	private String senderEmail = "sender@gmail.com";
	private String receiverEmail = "receiver@gmail.com";
	
	private User sender;
	private User receiver;
	private Friendship friendship;
	private FriendshipResponse friendshipResponse;
	private List<User> listFriends = new ArrayList<User>();
	private MultipartFile file;
	
	@BeforeEach
	void init() {
		friendship = Friendship.builder()
				.friendshipId(1).user(sender).friend(receiver).status(StatusFriend.FRIEND).build();
		friendshipResponse = FriendshipResponse.builder()
				.id(1).userName("receiver").status(StatusFriend.FRIEND).build();
		sender = User.builder()
				.userId(1).email(senderEmail).userName("sender").build();
		receiver = User.builder()
				.userId(2).email(receiverEmail).userName("receiver").build();
		file = new MockMultipartFile(
	            "avatar",
	            "avatar.png",
	            "image/png",
	            "avatar image content".getBytes()
	        );
	}
	
//	Function findByEmail ------------------------------------------------------
	@Test
	void testFindByEmail_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
		when(friendshipRepo.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendship));
		
		var response = userService.findByEmail(receiverEmail, token);
		assertThat(response.getUserName().equals(friendshipResponse.getUserName()));
	}
	
	@Test
	void testFindByEmail_emailSenderEqualReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		
		var response = userService.findByEmail(senderEmail, token);
		assertThat(response.getUserName() == null);
	}
	
	@Test
	void testFindByEmail_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.findByEmail(receiverEmail, token));
		assertThat(exception.getMessage().equals("Not found user " + senderEmail));
	}
	
	@Test
	void testFindByEmail_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findByEmail(receiverEmail)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.findByEmail(receiverEmail, token));
		assertThat(exception.getMessage().equals("Not found user " + receiverEmail));
	}
	
//	Function findById ------------------------------------------------------
	@Test
	void testFindById_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findById(any())).thenReturn(Optional.of(receiver));
		when(friendshipRepo.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendship));
		
		var response = userService.findById(1, token);
		assertThat(response.getUserName().equals(friendshipResponse.getUserName()));
	}
	
	@Test
	void testFindById_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.findById(1, token));
		assertThat(exception.getMessage().equals("Not found user " + senderEmail));
	}
	
	@Test
	void testFindById_notFoundReceiver_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findById(1)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.findById(1, token));
		assertThat(exception.getMessage().equals("Not found user id" + 1));
	}
	
//	Function findByUsername ------------------------------------------------------
	@Test
	void testFindByUsername_success() {
		listFriends.add(receiver);
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findByUserNameContaining(any())).thenReturn(listFriends);
		when(friendshipRepo.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendship));
		
		var response = userService.findByUsername(token, "receiver");
		assertThat(response.size() == 1);
	}
	
	@Test
	void testFindByUsername_listFriendsEmpty_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepo.findByUserNameContaining(any())).thenReturn(listFriends);
		when(friendshipRepo.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(friendship));
		
		var response = userService.findByUsername(token, "receiver");
		assertThat(response == null);
	}
	
	@Test
	void testFindByUsername_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.findByUsername(token, "receiver"));
		assertThat(exception.getMessage().equals("Not found user " + senderEmail));
	}

//	Function updateUsername ------------------------------------------------------
	@Test
	void testUpdateUserName_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		
		var response = userService.updateUsername(token, "update");
		assertThat(response.getUserName().equals("update"));
	}
	
	@Test
	void testUpdateUserName_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.updateUsername(token, "update"));
		assertThat(exception.getMessage().equals("Not found user " + senderEmail));
	}
	
//	Function changeAvatar ------------------------------------------------------
	@Test
	void testchangeAvatar_success() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		
		var response = userService.changeAvatar(token, file);
		assertThat(response.getImage_url() != null);
	}
	
	@Test
	void testchangeAvatar_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepo.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		var exception = assertThrows(RuntimeException.class, () -> userService.changeAvatar(token, file));
		assertThat(exception.getMessage().equals("Not found user " + senderEmail));
	}
}
