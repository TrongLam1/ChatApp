package com.microservice.gateway.repository;

import com.microservice.gateway.request.IntrospectRequest;
import com.microservice.gateway.response.IntrospectResponse;
import com.microservice.gateway.response.ResponseData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {

    // Http Interface Spring 6 (not Feign Client)
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseData<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
