package com.chat.app.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {

	@NotBlank
	private String groupName;
	
	@NotNull
	private Integer user1;
	
	@NotNull
	private Integer user2;
}
