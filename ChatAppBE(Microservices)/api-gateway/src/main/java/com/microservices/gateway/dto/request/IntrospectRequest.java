package com.microservice.gateway.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IntrospectRequest {

    private String token;
}
