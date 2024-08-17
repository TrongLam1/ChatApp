package com.microservices.account.service;

import com.microservices.account.dto.request.AuthenticationRequest;
import com.microservices.account.dto.request.IntrospectRequest;
import com.microservices.account.dto.request.RegistrationRequest;
import com.microservices.account.dto.response.AuthenticationResponse;
import com.microservices.account.dto.response.IntrospectResponse;
import com.microservices.account.dto.response.ProfileResponse;

public interface IAuthenticationService {

    public IntrospectResponse introspect(IntrospectRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public ProfileResponse registration(RegistrationRequest request);
}
