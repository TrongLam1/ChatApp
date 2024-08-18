package com.microservices.group.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_group_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column
    private String content;

    @Column
    private Long senderId;

    @Column
    private LocalDateTime createdDate;
}
