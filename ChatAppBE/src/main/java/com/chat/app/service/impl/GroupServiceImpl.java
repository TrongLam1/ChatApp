package com.chat.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.app.dto.GroupDTO;
import com.chat.app.dto.GroupMemberDTO;
import com.chat.app.exception.GroupException;
import com.chat.app.exception.UserException;
import com.chat.app.modal.Group;
import com.chat.app.modal.GroupMember;
import com.chat.app.modal.User;
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
			User user = userRepository.findByEmail(email);
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
			User creator = userRepository.findByEmail(emailCreator);
			User friend1 = findUserById(request.getUser1());
			User friend2 = findUserById(request.getUser2());

			Group newGroup = new Group();
			newGroup.setGroupId(generateCustomId());
			newGroup.setCreator(creator);
			newGroup.setGroupName(request.getGroupName());

			groupRepository.save(newGroup);

			GroupMember admin = createGroupMember(creator, newGroup);
			GroupMember member1 = createGroupMember(friend1, newGroup);
			GroupMember member2 = createGroupMember(friend2, newGroup);

			List<GroupMember> listMembers = new ArrayList<>();
			listMembers.add(admin);
			listMembers.add(member1);
			listMembers.add(member2);

			newGroup.setListMembers(listMembers);

			groupRepository.save(newGroup);

			return "Create group name " + request.getGroupName() + " success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String addFriendToGroup(Integer userId, String groupId) {
		try {
			User friend = findUserById(userId);
			Group group = findById(groupId);

			GroupMember member = createGroupMember(friend, group);
			group.getListMembers().add(member);

			groupRepository.save(group);

			return "Add friend to group " + groupId + " success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<GroupMemberDTO> getListMembersForGroup(String token, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email);

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
	public String removeUserFromGroup(Integer userId, String groupId) {
		try {
			Group group = findById(groupId);
			User user = findUserById(userId);
			GroupMember member = groupMemberRepository.findByUser(user)
					.orElseThrow(() -> new UserException("Not found member."));
			group.getListMembers().remove(member);

			groupMemberRepository.delete(member);
			groupRepository.save(group);

			return "Remove user success.";
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<GroupDTO> getListGroupsFromUser(String token) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email);
			List<Group> listGroups = groupMemberRepository.findListGroupsByUser(user);
			List<GroupDTO> listGroupsDTO = listGroups.stream().map(item -> mapper.map(item, GroupDTO.class))
					.collect(Collectors.toList());
			return listGroupsDTO;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
