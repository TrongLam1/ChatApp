package com.microservices.friendship.entity;

import com.microservices.friendship.entity.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime modifiedDate;

    @Column
    private Long userId;

    @Column
    private Long userFriendId;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}
