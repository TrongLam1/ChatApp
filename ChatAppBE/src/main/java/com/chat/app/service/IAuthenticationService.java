package com.chat.app.service;

import com.chat.app.request.AuthenRequest;
import com.chat.app.request.SignUpRequest;
import com.chat.app.response.JwtAuthenticationResponse;

public interface IAuthenticationService {

	String signUp(SignUpRequest request);
	JwtAuthenticationResponse signIn(AuthenRequest request);
	JwtAuthenticationResponse refreshToken(String token);
	String logOut(String token);
}
