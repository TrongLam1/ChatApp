package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.chat.app.exception.GroupException;
import com.chat.app.exception.UserException;
import com.chat.app.model.Friendship;
import com.chat.app.model.Group;
import com.chat.app.model.GroupMember;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.GroupMemberRepository;
import com.chat.app.repository.GroupRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.CreateGroupRequest;
import com.chat.app.service.impl.GroupServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

@SpringBootTest
public class GroupServiceTests {

	@Autowired
	private GroupServiceImpl groupService;
	
	@MockBean
	private GroupRepository groupRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private GroupMemberRepository groupMemberRepository;

	@MockBean
	private FriendshipRepository friendshipRepository;

	@MockBean
	private JwtServiceImpl jwtService;
	
	private final String token = "validToken";
	private final String senderEmail = "sender@gmail.com";
	private final String receiverEmail = "receiver@gmail.com";
	private final String receiver2Email = "receiver2@gmail.com";
	private final String receiver3Email = "receiver3@gmail.com";
	
	private User sender;
	private User receiver;
	private User receiver2;
	private User receiver3;
	private Group group;
	private GroupMember admin;
	private List<User> listFriends = new ArrayList<>();
	private List<GroupMember> listMembers = new ArrayList<>();
	private Set<Friendship> friendUsers = new HashSet<>();
	private CreateGroupRequest createGroupReq;
	
	@BeforeEach
	void init() {
		sender = User.builder()
				.userId(1).email(senderEmail).userName("sender").build();
		receiver = User.builder()
				.userId(2).email(receiverEmail).userName("receiver").build();
		receiver2 = User.builder()
				.userId(3).email(receiver2Email).userName("receiver2").build();
		receiver3 = User.builder()
				.userId(4).email(receiver3Email).userName("receiver3").build();

		admin = GroupMember.builder()
				.group(group).user(sender).build();
		listMembers.add(admin);
		group = Group.builder()
				.groupId("group1")
				.creator(sender)
				.listMembers(listMembers)
				.build();
		
		Set<Integer> friends = new HashSet<>();
		friends.add(2);
		friends.add(3);
		createGroupReq = CreateGroupRequest.builder()
				.groupName("Test")
				.listFriends(friends).build();
	}
	
//	Function createGroupChat --------------------------------------------------------------------
	@Test
	void testCreateGroupChat_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
		when(userRepository.findById(3)).thenReturn(Optional.of(receiver2));
		
		var res = groupService.createGroupChat(token, createGroupReq);
		assertThat(res).isEqualTo("Create group name " + createGroupReq.getGroupName() + " success.");
	}
	
	@Test
	void testCreateGroupChat_notFoundSender_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.createGroupChat(token, createGroupReq))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
	
//	Function addFriendsToGroup ------------------------------------------------------------------
	@Test
	void testAddFriendsToGroup_success() throws UserException {
		Set<Integer> ids = new HashSet<>();
		ids.add(4);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepository.findByGroupId("group1")).thenReturn(Optional.of(group));
		when(userRepository.findById(4)).thenReturn(Optional.of(receiver3));
		
		var res = groupService.addFriendsToGroup(token, ids, "group1");
		assertThat(res).isEqualTo("Add friends to group " + group.getGroupName() + " success.");
	}
	
	@Test
	void testAddFriendsToGroup_notFoundInviter_failed() {
		Set<Integer> ids = new HashSet<>();
		ids.add(4);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.addFriendsToGroup(token, ids, "group1"))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user");
	}
	
//	Function getListUsersToAddGroup -------------------------------------------------------------
	@Test
	void testGetListUsersToAddGroup_success() throws Exception {
		listFriends.add(receiver);
		listFriends.add(receiver2);
		listFriends.add(receiver3);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepository.findById("group1")).thenReturn(Optional.of(group));
		when(friendshipRepository.getListFriendsByUserAndStatus(sender, StatusFriend.FRIEND)).thenReturn(listFriends);
		
		var res = groupService.getListUsersToAddGroup(token, "group1");
		assertThat(res.size()).isEqualTo(3);
	}
	
	@Test
	void testGetListUsersToAddGroup_notFoundInviter_failed() {
		Set<Integer> ids = new HashSet<>();
		ids.add(4);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.getListUsersToAddGroup(token, "group1"))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user");
	}
	
	@Test
	void testGetListUsersToAddGroup_notFoundGroup_failed() {
		Set<Integer> ids = new HashSet<>();
		ids.add(4);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepository.findById("group1")).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.getListUsersToAddGroup(token, "group1"))
        .isInstanceOf(GroupException.class)
        .hasMessage("Not found group");
	}
//	Function getListGroupsFromUser --------------------------------------------------------------
	@Test
	void testGetListGroupsFromUser_success() throws UserException {
		List<Group> listGroups = new ArrayList<>();
		listGroups.add(group);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupMemberRepository.findListGroupsByUser(sender)).thenReturn(listGroups);
		
		var res = groupService.getListGroupsFromUser(token);
		assertThat(res.size()).isEqualTo(1);
	}
	
	@Test
	void testGetListGroupsFromUser_notFoundUser_failed() throws UserException {
		List<Group> listGroups = new ArrayList<>();
		listGroups.add(group);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.getListGroupsFromUser(token))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
 	
//	Function getListMembersForGroup -------------------------------------------------------------
	@Test
	void testGetListMembersForGroup_success() throws UserException {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepository.findByGroupId(any())).thenReturn(Optional.of(group));
		when(friendshipRepository.getRelationshipFromUser(sender)).thenReturn(friendUsers);
		
		var res = groupService.getListMembersForGroup(token, "group1");
		assertThat(res.size()).isEqualTo(0);
	}
	
	@Test
	void testGetListMembersForGroup_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.getListMembersForGroup(token, "group1"))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + senderEmail);
	}
//	Function removeMemberFromGroup --------------------------------------------------------------
	@Test
	void testRemoveMemberFromGroup_success() throws Exception {
		GroupMember member = GroupMember.builder()
				.group(group).user(receiver).build();
		group.getListMembers().add(member);
		
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
		when(groupRepository.findById(any())).thenReturn(Optional.of(group));
		when(groupMemberRepository.findByUserAndGroup(receiver, group)).thenReturn(Optional.of(member));
		
		var res = groupService.removeMemberFromGroup(token, "group1", 2);
		assertThat(res).isEqualTo("Remove user from group success.");
	}
	
	@Test
	void testRemoveMemberFromGroup_notFoundAdmin_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.removeMemberFromGroup(token, "group1", 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user");
	}
	
	@Test
	void testRemoveMemberFromGroup_notFoundGroup_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(groupRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.removeMemberFromGroup(token, "group1", 2))
        .isInstanceOf(GroupException.class)
        .hasMessage("Not found group");
	}
	
	@Test
	void testRemoveMemberFromGroup_isNotAdmin_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(receiver));
		when(groupRepository.findById(any())).thenReturn(Optional.of(group));
		
		assertThatThrownBy(() -> groupService.removeMemberFromGroup(token, "group1", 2))
        .isInstanceOf(GroupException.class)
        .hasMessage("You are not admin.");
	}
	
	@Test
	void testRemoveMemberFromGroup_notFoundUserRemoved_failed() {
		when(jwtService.extractUsername(token)).thenReturn(senderEmail);
		when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
		when(userRepository.findById(2)).thenReturn(Optional.empty());
		when(groupRepository.findById(any())).thenReturn(Optional.of(group));
		
		assertThatThrownBy(() -> groupService.removeMemberFromGroup(token, "group1", 2))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user");
	}
	
//	Function quitGroup --------------------------------------------------------------------------
	@Test
	void testQuitGroup_success() throws Exception {
		GroupMember member = GroupMember.builder()
				.user(receiver).group(group).build();
		
		when(jwtService.extractUsername(token)).thenReturn(receiverEmail);
		when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
		when(groupRepository.findById(any())).thenReturn(Optional.of(group));
		when(groupMemberRepository.findByUserAndGroup(receiver, group)).thenReturn(Optional.of(member));
		
		var res = groupService.quitGroup(token, "group1");
		assertThat(res).isEqualTo("Quit out group " + group.getGroupName());
	}
	
	@Test
	void testQuitGroup_notFoundUser_failed() {
		when(jwtService.extractUsername(token)).thenReturn(receiverEmail);
		when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.quitGroup(token, "group1"))
        .isInstanceOf(UserException.class)
        .hasMessage("Not found user " + receiverEmail);
	}
	
	@Test
	void testQuitGroup_notFoundGroup_failed() {
		when(jwtService.extractUsername(token)).thenReturn(receiverEmail);
		when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
		when(groupRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> groupService.quitGroup(token, "group1"))
        .isInstanceOf(GroupException.class)
        .hasMessage("Not found group " + group.getGroupId());
	}
}
