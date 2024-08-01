package com.chat.app.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.request.CreateGroupRequest;
import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.GroupServiceImpl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/group")
@Slf4j
public class GroupController {

	@Autowired
	private GroupServiceImpl groupService;
	
	@PostMapping
	public ResponseData<?> createNewGroup(
			@RequestHeader("Authorization") String token,
			@RequestBody @Valid CreateGroupRequest request) {
		try {
			log.info("Create new group.");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Create new group", 
					groupService.createGroupChat(jwtToken, request));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@GetMapping("/{groupId}")
	public ResponseData<?> findOneGroupById(
			@RequestHeader("Authorization") String token,
			@PathVariable("groupId") String groupId) {
		try {
			log.info("Find group id {}", groupId);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Find group", 
					groupService.findGroupById(jwtToken, groupId));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	
	@GetMapping("/members/{groupId}")
	public ResponseData<?> getMembersFromGroup(
			@RequestHeader("Authorization") String token,
			@PathVariable("groupId") String groupId) {
		try {
			log.info("Members from group.");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Members from group", 
					groupService.getListMembersForGroup(jwtToken, groupId));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@GetMapping("/lists-from-user")
	public ResponseData<?> getGroupsFromUser(@RequestHeader("Authorization") String token) {
		try {
			log.info("List groups from user.");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "List groups", 
					groupService.getListGroupsFromUser(jwtToken));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@GetMapping("/list-friends-invite/{group}")
	public ResponseData<?> getListFriendsInvite(@RequestHeader("Authorization") String token,
			@PathVariable("group") String groupId) {
		try {
			log.info("List users to invite.");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "List users to invite", 
					groupService.getListUsersToAddGroup(jwtToken, groupId));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@PutMapping("/add-user-to-group/{group}")
	public ResponseData<?> addUserToGroup(@RequestHeader("Authorization") String token,
			@PathVariable("group") String groupId,
			@RequestParam("users") Set<Integer> userIds) {
		try {
			log.info("Add user to group {}", groupId);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Add user to group", 
					groupService.addFriendsToGroup(jwtToken, userIds, groupId));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@DeleteMapping("/quit-group/{group}")
	public ResponseData<?> quitGroup(@RequestHeader("Authorization") String token,
			@PathVariable("group") String groupId) {
		try {
			log.info("Quit group {}", groupId);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Quit group", 
					groupService.quitGroup(jwtToken, groupId));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}
