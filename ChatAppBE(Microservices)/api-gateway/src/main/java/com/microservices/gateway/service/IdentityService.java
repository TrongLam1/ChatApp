package com.microservice.gateway.service;

import com.microservice.gateway.repository.IdentityClient;
import com.microservice.gateway.request.IntrospectRequest;
import com.microservice.gateway.response.IntrospectResponse;
import com.microservice.gateway.response.ResponseData;
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
