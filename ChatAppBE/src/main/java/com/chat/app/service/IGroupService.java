package com.chat.app.service;

import java.util.List;
import java.util.Set;

import com.chat.app.dto.GroupDTO;
import com.chat.app.dto.GroupMemberDTO;
import com.chat.app.dto.UserDTO;
import com.chat.app.request.CreateGroupRequest;
import com.chat.app.response.GroupResponse;

public interface IGroupService {

	String createGroupChat(String token, CreateGroupRequest request);
	
	String addFriendsToGroup(String token, Set<Integer> userIds, String groupId);
	
	GroupResponse findGroupById(String token, String groupId);
	
	List<UserDTO> getListUsersToAddGroup(String token, String groupId);
	
	List<GroupDTO> getListGroupsFromUser(String token);
	
	List<GroupMemberDTO> getListMembersForGroup(String token, String groupId);
	
	String quitGroup(String token, String groupId);
}
