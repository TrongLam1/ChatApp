package com.chat.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequest {

	private String subscribe;

	private String content;
	
	private String image_url;
	
	private String image_id;
}
