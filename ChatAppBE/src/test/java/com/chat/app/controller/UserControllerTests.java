package com.chat.app.controller;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.chat.app.modal.enums.StatusFriend;
import com.chat.app.response.FriendshipResponse;
import com.chat.app.service.impl.AccountServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;
import com.chat.app.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	@MockBean
	private AccountServiceImpl accountService;
	
	@MockBean
	private UserServiceImpl userService;
	
	private String token = "validToken";
	
	private String email = "test@gmail.com";
	
	private FriendshipResponse friendshipResponse;
	
	@BeforeEach
	void init() {
		friendshipResponse = FriendshipResponse.builder()
				.id(1).email(email).userName("test").status(StatusFriend.FRIEND).build();
	}
	
	@Test
	@WithUserDetails("test@gmail.com")
	void findByEmail_success() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(token);
		
		when(userService.findByEmail(email, token)).thenReturn(friendshipResponse);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/find-by-email/" + email)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("status", "").value(200));
	}
}
