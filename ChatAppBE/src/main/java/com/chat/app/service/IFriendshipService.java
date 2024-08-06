package com.chat.app.service;

import java.util.List;

import com.chat.app.exception.UserException;
import com.chat.app.response.FriendshipResponse;

public interface IFriendshipService {

	FriendshipResponse sendAddFriend(String token, Integer toUser) throws UserException;
	
	FriendshipResponse acceptAddFriend(String token, Integer userId) throws UserException;
	
	FriendshipResponse denyAcceptFriend(String token, Integer userId) throws UserException;
	
	FriendshipResponse cancelAddFriend(String token, Integer userId) throws UserException;
	
	List<FriendshipResponse> findFriendInListFriends(String token, String username) throws UserException;
	
	int countRequestsAddFriend(String token) throws UserException;
	
	String blockedUser(String token, Integer userId) throws UserException;
	
	List<FriendshipResponse> listUsersWaitingAccept(String token) throws UserException;
	
	List<FriendshipResponse> listFriendsByUser(String token) throws UserException;
}
