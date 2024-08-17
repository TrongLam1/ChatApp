package com.microservices.profile.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "profile-service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long userId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @Indexed(unique = true)
    private String email;

    private String username;
}
