package com.chat.app.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDTO {

	private String channelId;

	private LocalDateTime createAt;

	private UserDTO receiver;

	private UserDTO sender;

	private List<MessageDTO> listMessages;
}
