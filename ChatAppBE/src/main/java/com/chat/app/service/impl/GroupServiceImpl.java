package com.chat.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.app.dto.GroupDTO;
import com.chat.app.dto.GroupMemberDTO;
import com.chat.app.dto.UserDTO;
import com.chat.app.exception.GroupException;
import com.chat.app.exception.UserException;
import com.chat.app.model.Group;
import com.chat.app.model.GroupMember;
import com.chat.app.model.User;
import com.chat.app.model.enums.StatusFriend;
import com.chat.app.repository.FriendshipRepository;
import com.chat.app.repository.GroupMemberRepository;
import com.chat.app.repository.GroupRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.CreateGroupRequest;
import com.chat.app.response.GroupResponse;
import com.chat.app.service.IGroupService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GroupServiceImpl implements IGroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private JwtServiceImpl jwtService;

	private String generateCustomId() {
		long count = groupRepository.count();
		return "group" + count;
	}

	public Group findById(String groupId) throws GroupException {
		Group group = groupRepository.findByGroupId(groupId)
				.orElseThrow(() -> new GroupException(("Not found group " + groupId)));

		return group;
	}

	private User findUserById(Integer userId) throws UserException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("Not found user " + userId));
		return user;
	}

	private GroupMember createGroupMember(User user, Group group) {
		try {
			long countMember = groupMemberRepository.count();
			String memberId = "member" + countMember;
			
			GroupMember member = new GroupMember();
			member.setMemberId(memberId);
			member.setUser(user);
			member.setGroup(group);

			groupMemberRepository.save(member);

			return member;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public GroupResponse findGroupById(String token, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			Group group = findById(groupId);
			groupMemberRepository.findByUserAndGroup(user, group)
					.orElseThrow(() -> new UserException("Not found user in group"));

			GroupResponse response = new GroupResponse();
			response.setGroup(mapper.map(group, GroupDTO.class));
			response.setTotalMembers(group.getListMembers().size());
			
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String createGroupChat(String token, CreateGroupRequest request) {
		try {
			String emailCreator = jwtService.extractUsername(token);
			User creator = userRepository.findByEmail(emailCreator)
					.orElseThrow(() -> new UserException("Not found user " + emailCreator));
			Group newGroup = new Group();
			newGroup.setGroupId(generateCustomId());
			newGroup.setCreator(creator);
			newGroup.setGroupName(request.getGroupName());
			newGroup.setCreateAt(new Date());

			groupRepository.save(newGroup);
			
			GroupMember admin = createGroupMember(creator, newGroup);
			List<GroupMember> listMembers = new ArrayList<>();
			listMembers.add(admin);
			
			for (Integer userId : request.getListFriends()) {
				User user = findUserById(userId);
				GroupMember member = createGroupMember(user, newGroup);
				listMembers.add(member);
			}

			newGroup.setListMembers(listMembers);

			groupRepository.save(newGroup);

			return "Create group name " + request.getGroupName() + " success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String addFriendsToGroup(String token, Set<Integer> userIds, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User inviter = userRepository.findByEmail(email).orElseThrow(() -> new UserException("Not found user"));
			Group group = findById(groupId);
			
			List<GroupMember> listMembers = group.getListMembers();
			boolean memberInviter = listMembers.stream()
				    .anyMatch(item -> item.getUser().equals(inviter));
			if (memberInviter) {
				for (Integer id : userIds) {
					User friend = findUserById(id);
					boolean checkExistedUser = listMembers.stream().anyMatch(item -> item.getUser().equals(friend));
					if (!checkExistedUser) createGroupMember(friend, group);
				}
			} else {
				new UserException(inviter.getUserName() + " is not member of group");
			}

			return "Add friends to group " + group.getGroupName() + " success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<GroupMemberDTO> getListMembersForGroup(String token, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));;

			Group group = findById(groupId);
			groupMemberRepository.findByUserAndGroup(user, group)
					.orElseThrow(() -> new UserException("Not found user in group"));

			List<GroupMember> listMembers = group.getListMembers();
			List<GroupMemberDTO> listMembersDTO = listMembers.stream()
					.map(item -> mapper.map(item, GroupMemberDTO.class)).collect(Collectors.toList());

			return listMembersDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<GroupDTO> getListGroupsFromUser(String token) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException("Not found user " + email));
			List<Group> listGroups = groupMemberRepository.findListGroupsByUser(user);
			List<GroupDTO> listGroupsDTO = listGroups.stream().map(item -> mapper.map(item, GroupDTO.class))
					.collect(Collectors.toList());
			return listGroupsDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String quitGroup(String token, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupException("Not found group " + groupId));
			GroupMember member = groupMemberRepository.findByUserAndGroup(user, group)
					.orElseThrow(() -> new UserException("Not found user " + user.getEmail()));
			groupMemberRepository.delete(member);
			return "Quit out group " + group.getGroupName();
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<UserDTO> getListUsersToAddGroup(String token, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User inviter = userRepository.findByEmail(email).orElseThrow(() -> new UserException("Not found user"));
			
			Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupException("Not found group"));
			
			List<GroupMember> listMembers = group.getListMembers();
			
			boolean checkedInviter = listMembers.stream().anyMatch(item -> item.getUser().equals(inviter));
			if (!checkedInviter) throw new UserException("User is not in group");
			
			List<User> listFriends = friendshipRepository.getListFriendsByUserAndStatus(inviter, StatusFriend.FRIEND);
			List<UserDTO> listFriendsInvite = new ArrayList<UserDTO>();
			
			listFriends.stream().forEach(friend -> {
				if (!listMembers.stream().anyMatch(member -> member.getUser().equals(friend.getAccount().getUser()))) {
					listFriendsInvite.add(mapper.map(friend.getAccount().getUser(), UserDTO.class));
				}
			});
			
			return listFriendsInvite;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
