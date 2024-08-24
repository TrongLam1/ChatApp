package com.microservices.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private Long subscribeId;

    private String sender;

    private String content;

    private LocalDateTime createdDate;
}
