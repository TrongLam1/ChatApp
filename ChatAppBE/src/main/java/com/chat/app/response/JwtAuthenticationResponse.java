package com.chat.app.response;

import java.io.Serializable;
import java.util.Date;

import com.chat.app.model.enums.Role;

import lombok.Data;

@Data
public class JwtAuthenticationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8423612549921956208L;

	private String token;
	
	private String refreshToken;

	private Integer userId;
	
	private String name;
	
	private Date expiredTime;
	
	private Role role;
	
	private String avatar;
}
