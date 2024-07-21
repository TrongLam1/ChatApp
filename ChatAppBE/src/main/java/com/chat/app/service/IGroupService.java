package com.chat.app.service;

import java.util.List;

import com.chat.app.dto.GroupDTO;
import com.chat.app.dto.GroupMemberDTO;
import com.chat.app.request.CreateGroupRequest;
import com.chat.app.response.GroupResponse;

public interface IGroupService {

	String createGroupChat(String token, CreateGroupRequest request);
	
	String addFriendToGroup(Integer userId, String groupId);
	
	GroupResponse findGroupById(String token, String groupId);
	
	List<GroupDTO> getListGroupsFromUser(String token);
	
	List<GroupMemberDTO> getListMembersForGroup(String token, String groupId);
	
	String removeUserFromGroup(Integer userId, String groupId);
}
