package com.chat.app.service;

import java.util.List;

import com.chat.app.response.FriendshipResponse;

public interface IFriendshipService {

	FriendshipResponse sendAddFriend(String token, Integer toUser);
	
	String acceptAddFriend(String token, Integer userId);
	
	String denyAcceptFriend(String token, Integer userId);
	
	String blockedUser(String token, Integer userId);
	
	List<FriendshipResponse> listUsersWaitingAccept(String token);
	
	List<FriendshipResponse> listFriendsByUser(String token);
}
