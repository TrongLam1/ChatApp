package com.chat.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_channel_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelMessages {

	@Id
	@Column(name = "channel_message_id")
	private String channnelMessageId;
	
	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;
	
	@ManyToOne
	@JoinColumn(name = "channel_id")
	private Channel channel;
	
	private String content;
	
	private String image_url;
	
	private String image_id;
	
	private LocalDateTime createAt;
}
