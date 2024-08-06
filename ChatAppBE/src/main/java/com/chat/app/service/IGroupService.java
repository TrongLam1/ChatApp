package com.chat.app.service;

import java.util.List;
import java.util.Set;

import com.chat.app.dto.GroupDTO;
import com.chat.app.dto.UserDTO;
import com.chat.app.exception.UserException;
import com.chat.app.request.CreateGroupRequest;
import com.chat.app.response.FriendshipResponse;
import com.chat.app.response.GroupResponse;

public interface IGroupService {

	String createGroupChat(String token, CreateGroupRequest request) throws UserException;
	
	String addFriendsToGroup(String token, Set<Integer> userIds, String groupId) throws UserException;
	
	GroupResponse findGroupById(String token, String groupId) throws UserException;
	
	List<UserDTO> getListUsersToAddGroup(String token, String groupId) throws Exception;
	
	List<GroupDTO> getListGroupsFromUser(String token) throws UserException;
	
	List<FriendshipResponse> getListMembersForGroup(String token, String groupId) throws UserException;
	
	String removeMemberFromGroup(String token, String groupId, Integer memberId) throws Exception;
	
	String quitGroup(String token, String groupId) throws Exception;
}
