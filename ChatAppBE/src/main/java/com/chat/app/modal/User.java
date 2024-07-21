package com.chat.app.modal;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@CreationTimestamp
	@Setter(value = AccessLevel.NONE)
	private LocalDateTime createAt;
	
	@LastModifiedDate
	@Setter(value = AccessLevel.NONE)
	private LocalDateTime modifiedDate;
	
	@NotBlank(message = "Email is mandatory.")
	private String email;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(unique = true)
	@Pattern(regexp = "\\d{10}")
	private String phone;
	
	@OneToOne(mappedBy = "user")
	private Account account;
	
//	@OneToMany(mappedBy = "receiver")
//    private List<Channel> receivedChannels;
//
//    @OneToMany(mappedBy = "sender")
//    private List<Channel> sentChannels;
}
