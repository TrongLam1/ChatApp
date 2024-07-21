package com.chat.app.service;

import java.util.List;

import com.chat.app.dto.UserDTO;
import com.chat.app.response.FriendshipResponse;

public interface IUserService {

	FriendshipResponse findByEmail(String email, String token);
	
	FriendshipResponse findById(Integer userId, String token);
	
	List<FriendshipResponse> findByUsername(String token, String username);
	
	UserDTO findById(Integer userId);
}