package com.chat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping("/find-by-email/{email}")
	public ResponseData<?> findByEmail(@RequestHeader("Authorization") String token, 
			@PathVariable("email") String email) {
		try {
			log.info("Find user email {}", email);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Find user", userService.findByEmail(email, jwtToken));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@GetMapping("/find-by-id/{id}")
	public ResponseData<?> findById(@RequestHeader("Authorization") String token, 
			@PathVariable("id") Integer id) {
		try {
			log.info("Find user id {}", id);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Find user", userService.findById(id, jwtToken));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
	
	@GetMapping("/find-by-username/{username}")
	public ResponseData<?> findByUsername(@RequestHeader("Authorization") String token,
			@PathVariable("username") String username) {
		try {
			log.info("Find users username {}", username);
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), "Find users", userService.findByUsername(jwtToken, username));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}
