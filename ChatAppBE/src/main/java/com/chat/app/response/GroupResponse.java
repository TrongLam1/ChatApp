package com.chat.app.response;

import com.chat.app.dto.GroupDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {

	private int totalMembers;
	
	private GroupDTO group;
}
