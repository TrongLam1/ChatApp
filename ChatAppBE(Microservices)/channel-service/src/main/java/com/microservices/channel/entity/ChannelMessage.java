package com.microservices.channel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long senderId;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private String content;

    private String image_url;

    private String image_id;

    @Column
    private LocalDateTime createdDate;
}
