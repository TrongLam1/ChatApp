package com.chat.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.chat.app.modal.enums.StatusFriend;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.response.FriendshipResponse;
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
	
	private String token = "validToken";
	private String email = "test@gmail.com";
	private FriendshipResponse friendshipResponse;
	
	@BeforeEach
	void init() {
		friendshipResponse = FriendshipResponse.builder()
				.id(1).email(email).userName("test").status(StatusFriend.FRIEND).build();
	}
}
