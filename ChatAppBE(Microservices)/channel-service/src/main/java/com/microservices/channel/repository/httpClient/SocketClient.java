package com.microservices.channel.repository.httpClient;

import com.microservices.channel.config.AuthenticationRequestInterceptor;
import com.microservices.channel.dto.response.MessageResponse;
import com.microservices.channel.dto.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "socket", url = "${app.services.socket}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface SocketClient {

    @PostMapping(value = "/send-message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<String> sendMessage(@RequestBody MessageResponse message);
}
