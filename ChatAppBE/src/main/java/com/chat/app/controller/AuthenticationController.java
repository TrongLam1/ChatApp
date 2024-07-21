package com.chat.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.chat.app.request.AuthenRequest;
import com.chat.app.request.SignUpRequest;
import com.chat.app.response.ResponseData;
import com.chat.app.response.ResponseError;
import com.chat.app.service.impl.AuthenticationServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/authentication")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationServiceImpl authenticationService;

	@PostMapping("/sign-up")
	public ResponseData<?> signUp(@Valid @RequestBody SignUpRequest request) {
		try {
			log.info("Sign up email: {}", request.getEmail());
			request.setEmail(HtmlUtils.htmlEscape(request.getEmail()));
			request.setPassword(HtmlUtils.htmlEscape(request.getPassword()));
			request.setUsername(HtmlUtils.htmlEscape(request.getUsername()));
			return new ResponseData<>(HttpStatus.CREATED.value(), "Đăng kí thành công.",
					authenticationService.signUp(request));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@PostMapping("/sign-in")
	public ResponseData<?> signIn(@Valid @RequestBody AuthenRequest request) {
		try {
			log.info("Sign in email: {}", request.getEmail());
			request.setEmail(HtmlUtils.htmlEscape(request.getEmail()));
			request.setPassword(HtmlUtils.htmlEscape(request.getPassword()));
			return new ResponseData<>(HttpStatus.OK.value(), "Đăng nhập thành công.",
					authenticationService.signIn(request));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	@GetMapping("/refresh-token/{token}")
	public ResponseData<?> refreshToken(@PathVariable("token") String token) {
		try {
			log.info("Refresh token");
			return new ResponseData<>(HttpStatus.OK.value(), "Refresh token success",
					authenticationService.refreshToken(token));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), "Refresh token failed.");
		}
	}

	@GetMapping("/log-out")
	public ResponseData<?> logOut(@RequestHeader("Authorization") String token) {
		try {
			log.info("Log out");
			String jwtToken = token.substring(7);
			return new ResponseData<>(HttpStatus.OK.value(), authenticationService.logOut(jwtToken));
		} catch (Exception e) {
			log.error("errorMessage={}", e.getMessage(), e.getCause());
			return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), "Reset pass failed.");
		}
	}
}
