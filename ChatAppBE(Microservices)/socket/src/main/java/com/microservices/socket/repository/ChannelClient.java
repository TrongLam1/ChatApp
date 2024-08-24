package com.microservices.socket.repository;

import com.microservices.socket.config.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "channel-service", url = "${app.services.channel}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ChannelClient {


}
