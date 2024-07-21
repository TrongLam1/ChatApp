package com.chat.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
	
	private String jwt;
	private boolean isAuth;
}
