package com.microservices.profile.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private String id;

    private Long userId;

    private String email;

    private String username;
}
