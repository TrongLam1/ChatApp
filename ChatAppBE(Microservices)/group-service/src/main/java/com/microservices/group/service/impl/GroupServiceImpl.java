package com.microservices.group.service.impl;

import com.microservices.group.dto.request.GroupCreationRequest;
import com.microservices.group.dto.response.FriendshipResponse;
import com.microservices.group.dto.response.GroupResponse;
import com.microservices.group.dto.response.ProfileResponse;
import com.microservices.group.repository.GroupMemberRepository;
import com.microservices.group.repository.GroupRepository;
import com.microservices.group.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements IGroupService {

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    @Override
    public String createGroupChat(GroupCreationRequest request) {
        return "";
    }

    @Override
    public String addFriendsToGroup(Set<Integer> userIds, String groupId) {
        return "";
    }

    @Override
    public GroupResponse findGroupById(String groupId) {
        return null;
    }

    @Override
    public List<ProfileResponse> getListUsersToAddGroup(String groupId) {
        return List.of();
    }

    @Override
    public List<GroupResponse> getListGroupsFromUser() {
        return List.of();
    }

    @Override
    public List<FriendshipResponse> getListMembersForGroup(String groupId) {
        return List.of();
    }

    @Override
    public String removeMemberFromGroup(String groupId, Integer memberId) {
        return "";
    }

    @Override
    public String quitGroup(String groupId) {
        return "";
    }
}
