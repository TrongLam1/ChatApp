package com.microservices.account.repository.httpClient;

import com.microservices.account.config.AuthenticationRequestInterceptor;
import com.microservices.account.dto.request.ProfileCreationRequest;
import com.microservices.account.dto.response.ProfileResponse;
import com.microservices.account.dto.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @PostMapping(value = "/internal/new", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<ProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);

}
