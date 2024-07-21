package com.chat.app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberDTO {

	private String memberId;

	private LocalDateTime joinedAt;

	private LocalDateTime modifiedDate;

	private UserDTO user;
}
