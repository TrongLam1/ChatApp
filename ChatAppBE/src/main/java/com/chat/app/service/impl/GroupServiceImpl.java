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
import com.chat.app.dto.MessageDTO;
import com.chat.app.dto.UserDTO;
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
import com.chat.app.response.FriendshipResponse;
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
		return "group" + groupRepository.count();
	}

	public Group findById(String groupId) throws GroupException {
		return groupRepository.findByGroupId(groupId)
				.orElseThrow(() -> new GroupException(("Not found group " + groupId)));
	}

	private User findUserById(Integer userId) throws UserException {
		return userRepository.findById(userId).orElseThrow(() -> new UserException("Not found user " + userId));
	}

	private GroupMember createGroupMember(User user, Group group) {
		try {
			long countMember = groupMemberRepository.count();
			String memberId = "member" + countMember;

			GroupMember member = new GroupMember();
			member.setMemberId(memberId);
			member.setUser(user);
			member.setGroup(group);

			member = groupMemberRepository.save(member);

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
			
			if (group.getLastMessage() != null) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setContent(group.getLastMessage().getContent());
				messageDTO.setCreateAt(group.getLastMessage().getCreateAt().toString());
				messageDTO.setImage_url(group.getLastMessage().getImage_url());
				messageDTO.setSender(group.getLastMessage().getSender().getUserName());
				
				response.getGroup().setLastMessage(messageDTO);
			}
			
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

			Set<User> listMembers = group.getListMembers()
					.stream().map(item -> item.getUser())
					.collect(Collectors.toSet());
			if (listMembers.contains(inviter)) {
				for (Integer id : userIds) {
					User friend = findUserById(id);
					if (!listMembers.contains(friend))
						createGroupMember(friend, group);
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
	public List<FriendshipResponse> getListMembersForGroup(String token, String groupId) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));

			Group group = findById(groupId);

			Set<User> listMembers = group.getListMembers().stream()
					.map(member -> member.getUser())
					.collect(Collectors.toSet());
			
			if (!listMembers.contains(user)) throw new UserException("Not found user in group");
			
			Set<Friendship> friendUsers = friendshipRepository.getRelationshipFromUser(user);

			return listMembers.stream()
					.filter(member -> !member.equals(user))
					.map(member -> {
				FriendshipResponse response = new FriendshipResponse();
				response.setAvatar(member.getImage_url());
				response.setUserName(member.getUserName());
				response.setId(member.getUserId());
				
				Friendship friendship = friendUsers.stream()
	                    .filter(f -> f.getFriend().equals(member))
	                    .findFirst()
	                    .orElse(null);
				
				if (friendship != null) {
	                response.setStatus(friendship.getStatus());
	            } else {
	                response.setStatus(null);
	            }
				return response;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public List<GroupDTO> getListGroupsFromUser(String token) {
		try {
			String email = jwtService.extractUsername(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserException("Not found user " + email));
			List<Group> listGroups = groupMemberRepository.findListGroupsByUser(user);
			
			return listGroups.stream()
					.map(item -> {
						GroupDTO dto = new GroupDTO();
						dto.setGroupId(item.getGroupId());
						dto.setGroupName(item.getGroupName());
						
						if (item.getLastMessage() != null) {
							MessageDTO messageDTO = new MessageDTO();
							messageDTO.setContent(item.getLastMessage().getContent());
							messageDTO.setCreateAt(item.getLastMessage().getCreateAt().toString());
							messageDTO.setImage_url(item.getLastMessage().getImage_url());
							messageDTO.setSender(item.getLastMessage().getSender().getUserName());
							
							dto.setLastMessage(messageDTO);
						}
						
						return dto;
					})
					.collect(Collectors.toList());
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
			Group group = groupRepository.findById(groupId)
					.orElseThrow(() -> new GroupException("Not found group " + groupId));
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

			Set<User> listMembers = group.getListMembers().stream()
					.map(item -> item.getUser())
					.collect(Collectors.toSet());

			// check is user in group ?
			if (!listMembers.contains(inviter))
				throw new UserException("User is not in group");

			List<User> listFriends = friendshipRepository.getListFriendsByUserAndStatus(inviter, StatusFriend.FRIEND);

			// loop if users in list friends is not in group -> get
			return listFriends.stream()
					.filter(friendUser -> !listMembers.contains(friendUser))
					.map(friendUser -> mapper.map(friendUser, UserDTO.class)).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String removeMemberFromGroup(String token, String groupId, Integer memberId) throws Exception {
		try {
			String email = jwtService.extractUsername(token);
			User admin = userRepository.findByEmail(email).orElseThrow(() -> new UserException("Not found user"));
			Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupException("Not found group"));
			
			if (!group.getCreator().equals(admin)) throw new GroupException("You are not admin.");
			
			User removeUser = userRepository.findById(memberId).orElseThrow(() -> new UserException("Not found user"));
			GroupMember removeMember = groupMemberRepository.findByUserAndGroup(removeUser, group)
					.orElseThrow(() -> new UserException("Not found user in group"));
			
			groupMemberRepository.delete(removeMember);
			
			return "Remove user from group success.";
		} catch (UserException | GroupException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("An error occurred while retrieving friendship responses", e);
	    }
	}
}
