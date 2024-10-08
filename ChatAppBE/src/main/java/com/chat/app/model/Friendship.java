package com.chat.app.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.chat.app.model.enums.StatusFriend;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tbl_friend")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@IdClass(FriendId.class)
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class Friendship {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer friendshipId;

//	@Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

 //   @Id
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;
	
	@CreationTimestamp
	@Setter(value = AccessLevel.NONE)
	private LocalDateTime createAt;
	
	@LastModifiedDate
	@Setter(value = AccessLevel.NONE)
	private LocalDateTime modifiedDate;
	
	@Enumerated(EnumType.STRING)
	private StatusFriend status;
}
