package com.microservices.gateway.repository;

import com.microservices.gateway.dto.request.IntrospectRequest;
import com.microservices.gateway.dto.response.IntrospectResponse;
import com.microservices.gateway.dto.response.ResponseData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {

    // Http Interface Spring 6 (not Feign Client)
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseData<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
