package com.chat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.FriendshipServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/friendship")
@Slf4j
public class FriendshipController {

	@Autowired
	private FriendshipServiceImpl friendshipService;

	@GetMapping("/list-friends")
	public ResponseData<?> getListFriends(@RequestHeader("Authorization") String token) {
		try {
			log.info("List friends");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "List friends",
					friendshipService.listFriendsByUser(jwtToken));
		} catch (Exception e) {
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@GetMapping("/list-friends-waiting-accept")
	public ResponseData<?> getListFriendsWatingAccept(@RequestHeader("Authorization") String token) {
		try {
			log.info("List friends waiting accept");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "List friends waiting accept",
					friendshipService.listUsersWaitingAccept(jwtToken));
		} catch (Exception e) {
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@GetMapping("/send-add-friend/{friend}")
	public ResponseData<?> sendAddFriend(@RequestHeader("Authorization") String token,
			@PathVariable("friend") Integer id) {
		try {
			log.info("Send add friend to id {}", id);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Send request add friend", 
					friendshipService.sendAddFriend(jwtToken, id));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@GetMapping("/add-friend/{friend}")
	public ResponseData<?> addFriend(@RequestHeader("Authorization") String token, @PathVariable("friend") Integer id) {
		try {
			log.info("Add friend");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Add friend",
					friendshipService.acceptAddFriend(jwtToken, id));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@DeleteMapping("/deny-add-friend/{friend}")
	public ResponseData<?> denyAddFriend(@RequestHeader("Authorization") String token, @PathVariable("friend") Integer id) {
		try {
			log.info("Deny add friend");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Deny add friend",
					friendshipService.denyAcceptFriend(jwtToken, id));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@DeleteMapping("/cancel-add-friend/{friend}")
	public ResponseData<?> cancelAddFriend(@RequestHeader("Authorization") String token, @PathVariable("friend") Integer id) {
		try {
			log.info("Cancel add friend");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Cancel add friend", 
					friendshipService.cancelAddFriend(jwtToken, id));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@GetMapping("/count-request-add-friend")
	public ResponseData<?> countRequestsAddFriends(@RequestHeader("Authorization") String token) {
		try {
			log.info("Count requests add friend");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Count requests add friend", 
					friendshipService.countRequestsAddFriend(jwtToken));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}
