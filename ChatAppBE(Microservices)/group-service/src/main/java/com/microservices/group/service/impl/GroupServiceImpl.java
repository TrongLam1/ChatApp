package com.microservices.group.service.impl;

import com.microservices.group.dto.request.GroupCreationRequest;
import com.microservices.group.dto.response.FriendshipResponse;
import com.microservices.group.dto.response.GroupResponse;
import com.microservices.group.dto.response.ProfileResponse;
import com.microservices.group.entity.Group;
import com.microservices.group.entity.GroupMember;
import com.microservices.group.exception.GroupException;
import com.microservices.group.repository.GroupMemberRepository;
import com.microservices.group.repository.GroupRepository;
import com.microservices.group.repository.httpClient.FriendshipClient;
import com.microservices.group.repository.httpClient.ProfileClient;
import com.microservices.group.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements IGroupService {

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final FriendshipClient friendshipClient;

    private final ProfileClient profileClient;

    @Override
    public String createGroupChat(GroupCreationRequest request) {
        try {
            Long creatorId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Group group = Group.builder()
                    .groupName(request.getGroupName())
                    .admin(creatorId)
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .groupMembers(new ArrayList<>())
                    .groupMessages(new ArrayList<>())
                    .build();

            group = groupRepository.save(group);

            Set<GroupMember> listMembers = new HashSet<>(group.getGroupMembers());
            Set<Long> listMembersId = listMembers.stream().map(GroupMember::getUserId).collect(Collectors.toSet());

            createGroupMember(creatorId, group, listMembersId);

            for (Long userId : request.getListFriends()) {
                createGroupMember(userId, group, listMembersId);
            }

            return "Successfully create group chat.";
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public String addFriendsToGroup(Set<Long> userIds, Long groupId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Group group = findGroupByGroupId(groupId);
            groupMemberRepository.findByUserIdAndGroup(userId, group)
                    .orElseThrow(() -> new GroupException("Not found member in group"));

            Set<GroupMember> listMembers = new HashSet<>(group.getGroupMembers());
            Set<Long> listMembersId = listMembers.stream().map(GroupMember::getUserId).collect(Collectors.toSet());
            for (Long id : userIds) {
                createGroupMember(id, group, listMembersId);
            }

            return "Successfully add friends.";
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public GroupResponse findGroupById(Long groupId) {
        try {
            Group group = findGroupByGroupId(groupId);
            return GroupResponse.builder()
                    .admin(group.getAdmin())
                    .groupName(group.getGroupName())
                    .totalMembers(group.getGroupMembers().size())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<ProfileResponse> getListUsersToAddGroup(Long groupId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Group group = findGroupByGroupId(groupId);
            groupMemberRepository.findByUserIdAndGroup(userId, group)
                    .orElseThrow(() -> new GroupException("Not found member"));

            List<GroupMember> membersGroup = group.getGroupMembers();
            Set<Long> membersIdGroup = membersGroup.stream().map(GroupMember::getUserId).collect(Collectors.toSet());

            List<FriendshipResponse> listFriendsUser = friendshipClient.getListFriendsFromUser().getData();
            Set<Long> listFriendsId = listFriendsUser.stream().map(FriendshipResponse::getUserId).collect(Collectors.toSet());

            Set<Long> listFriendsIdToAdd = new HashSet<Long>();
            for (Long friendId : listFriendsId) {
                if (!membersIdGroup.contains(friendId)) listFriendsIdToAdd.add(friendId);
            }

            List<ProfileResponse> listFriendsToAdd = new ArrayList<>();
            for (Long friendId : listFriendsIdToAdd) {
                ProfileResponse profile = profileClient.getProfileById(friendId).getData();
                listFriendsToAdd.add(profile);
            }

            return listFriendsToAdd;
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<GroupResponse> getListGroupsFromUser() {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            List<Group> listGroups = groupMemberRepository.findByUserId(userId);
            return listGroups.stream().map(group ->
                            GroupResponse.builder()
                                    .admin(group.getAdmin())
                                    .groupName(group.getGroupName())
                                    .totalMembers(group.getGroupMembers().size())
                                    .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public List<FriendshipResponse> getListMembersForGroup(Long groupId) {
        return List.of();
    }

    @Override
    public String removeMemberFromGroup(Long groupId, Long memberId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Group group = findGroupByGroupId(groupId);

            if (!group.getAdmin().equals(userId)) throw new GroupException("You are not admin");
            GroupMember member = groupMemberRepository.findByUserIdAndGroup(memberId, group)
                    .orElseThrow(() -> new GroupException("Not found member"));
            groupMemberRepository.delete(member);

            return "Successfully remove user.";
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public String quitGroup(Long groupId) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            Group group = findGroupByGroupId(groupId);
            GroupMember member = groupMemberRepository.findByUserIdAndGroup(userId, group)
                    .orElseThrow(() -> new GroupException("Not found member"));
            groupMemberRepository.delete(member);

            return "Successfully quit out group.";
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private Group findGroupByGroupId(Long groupId) throws GroupException {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupException("Not found group"));
    }

    private void createGroupMember(Long userId, Group group, Set<Long> listMembersId) {
        try {
            if (!listMembersId.contains(userId)) {
                groupMemberRepository.save(
                        GroupMember.builder()
                                .group(group)
                                .userId(userId)
                                .createdDate(LocalDateTime.now())
                                .modifiedDate(LocalDateTime.now())
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
