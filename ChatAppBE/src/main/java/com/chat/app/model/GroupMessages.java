package com.chat.app.modal;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tbl_group_message")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GroupMessages {

	@Id
	@Column(name = "group_message_id")
	private String groupMessageId;
	
	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;
	
	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;
	
	private String content;
	
	@CreationTimestamp
	@Setter(value = AccessLevel.NONE)
	private LocalDateTime createAt;
}
