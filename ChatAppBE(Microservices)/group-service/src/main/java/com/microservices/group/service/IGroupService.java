package com.microservices.group.service;

import com.microservices.group.dto.request.GroupCreationRequest;
import com.microservices.group.dto.response.FriendshipResponse;
import com.microservices.group.dto.response.GroupResponse;
import com.microservices.group.dto.response.ProfileResponse;

import java.util.List;
import java.util.Set;

public interface IGroupService {

    String createGroupChat(GroupCreationRequest request);

    String addFriendsToGroup(Set<Integer> userIds, String groupId);

    GroupResponse findGroupById(String groupId);

    List<ProfileResponse> getListUsersToAddGroup(String groupId);

    List<GroupResponse> getListGroupsFromUser();

    List<FriendshipResponse> getListMembersForGroup(String groupId);

    String removeMemberFromGroup(String groupId, Integer memberId);

    String quitGroup(String groupId);
}
