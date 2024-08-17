package com.microservices.profile.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {

    private Long userId;

    private String email;

    private String username;
}
