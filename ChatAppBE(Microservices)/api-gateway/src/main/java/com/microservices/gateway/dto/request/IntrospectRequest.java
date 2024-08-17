package com.microservices.gateway.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IntrospectRequest {

    private String token;
}
