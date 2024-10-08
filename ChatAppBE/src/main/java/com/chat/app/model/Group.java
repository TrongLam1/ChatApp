package com.chat.app.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_group")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Group {

	@Id
	@Column(name = "group_id")
	private String groupId;

	private String groupName;

	@ManyToOne
	@JoinColumn(name = "creator_id")
	private User creator;

	private Date createAt;

	@LastModifiedDate
	@Setter(value = AccessLevel.NONE)
	private Date modifiedDate;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private List<GroupMessages> listMessages;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private List<GroupMember> listMembers;

	@OneToOne
    @JoinColumn(name = "last_message_id")
	private GroupMessages lastMessage;
}
