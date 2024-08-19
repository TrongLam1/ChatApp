package com.microservices.group.service;

import com.microservices.group.dto.request.GroupCreationRequest;
import com.microservices.group.dto.response.FriendshipResponse;
import com.microservices.group.dto.response.GroupResponse;
import com.microservices.group.dto.response.ProfileResponse;

import java.util.List;
import java.util.Set;

public interface IGroupService {

    String createGroupChat(GroupCreationRequest request);

    String addFriendsToGroup(Set<Long> userIds, Long groupId);

    GroupResponse findGroupById(Long groupId);

    List<ProfileResponse> getListUsersToAddGroup(Long groupId);

    List<GroupResponse> getListGroupsFromUser();

    List<FriendshipResponse> getListMembersForGroup(Long groupId);

    String removeMemberFromGroup(Long groupId, Long memberId);

    String quitGroup(Long groupId);
}
