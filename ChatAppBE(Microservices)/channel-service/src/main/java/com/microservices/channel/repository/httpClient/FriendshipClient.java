package com.microservices.channel.repository.httpClient;

import com.microservices.channel.config.AuthenticationRequestInterceptor;
import com.microservices.channel.dto.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "friendship-service", url = "${app.services.friendship}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface FriendshipClient {

    @GetMapping(value = "/checked-status-friend/{friendId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<Boolean> checkStatusFriend(@PathVariable Long friendId);

}
