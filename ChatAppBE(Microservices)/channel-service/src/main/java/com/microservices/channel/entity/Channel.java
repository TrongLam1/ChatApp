package com.microservices.channel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long friendId;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<ChannelMessage> listMessages;
}
