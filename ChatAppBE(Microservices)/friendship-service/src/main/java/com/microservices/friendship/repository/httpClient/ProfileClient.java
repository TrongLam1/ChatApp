package com.microservices.friendship.repository.httpClient;

import com.microservices.friendship.config.AuthenticationRequestInterceptor;
import com.microservices.friendship.dto.response.ProfileResponse;
import com.microservices.friendship.dto.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @GetMapping(value = "/get-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<ProfileResponse> getProfile();

    @GetMapping(value = "/get-profile/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<ProfileResponse> getProfileById(@PathVariable Long userId);

    @GetMapping(value = "/get-profiles-username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<List<ProfileResponse>> getProfilesByUsername(@PathVariable String username);
}
