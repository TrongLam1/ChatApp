package com.microservices.gateway.service;

import com.microservices.gateway.dto.request.IntrospectRequest;
import com.microservices.gateway.dto.response.IntrospectResponse;
import com.microservices.gateway.dto.response.ResponseData;
import com.microservices.gateway.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final IdentityClient identityClient;

    public Mono<ResponseData<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }
}
