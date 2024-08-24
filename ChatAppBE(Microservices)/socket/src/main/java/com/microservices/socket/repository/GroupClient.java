package com.microservices.socket.repository;

import com.microservices.socket.config.AuthenticationRequestInterceptor;
import com.microservices.socket.dto.request.MessageRequest;
import com.microservices.socket.dto.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "group-service", url = "${app.services.group}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface GroupClient {

    @PostMapping(value = "/group-message", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<String> sendMessage(@RequestBody MessageRequest request);
}
