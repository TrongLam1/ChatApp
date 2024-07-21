package com.chat.app.response;

import com.chat.app.modal.enums.StatusFriend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendshipResponse {

	private Integer id;

	private String email;
	
	private String userName;
	
	private StatusFriend status;
}
