package com.microservices.account.controller;

import com.microservices.account.dto.request.AuthenticationRequest;
import com.microservices.account.dto.request.IntrospectRequest;
import com.microservices.account.dto.request.RegistrationRequest;
import com.microservices.account.dto.response.AuthenticationResponse;
import com.microservices.account.dto.response.IntrospectResponse;
import com.microservices.account.dto.response.ProfileResponse;
import com.microservices.account.dto.response.ResponseData;
import com.microservices.account.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/introspect")
    public ResponseData<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Introspect",
                    authenticationService.introspect(request));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/registration")
    public ResponseData<ProfileResponse> registration(@RequestBody RegistrationRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Registration",
                    authenticationService.registration(request));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseData<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Authenticate",
                    authenticationService.authenticate(request));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
