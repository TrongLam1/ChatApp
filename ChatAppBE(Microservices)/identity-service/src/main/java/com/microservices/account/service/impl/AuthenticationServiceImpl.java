package com.microservices.account.service.impl;

import com.microservices.account.constant.PredefinedRole;
import com.microservices.account.dto.request.AuthenticationRequest;
import com.microservices.account.dto.request.IntrospectRequest;
import com.microservices.account.dto.request.ProfileCreationRequest;
import com.microservices.account.dto.request.RegistrationRequest;
import com.microservices.account.dto.response.AuthenticationResponse;
import com.microservices.account.dto.response.IntrospectResponse;
import com.microservices.account.dto.response.ProfileResponse;
import com.microservices.account.entity.Role;
import com.microservices.account.entity.User;
import com.microservices.account.exception.UserException;
import com.microservices.account.repository.RoleRepository;
import com.microservices.account.repository.UserRepository;
import com.microservices.account.repository.httpClient.ProfileClient;
import com.microservices.account.service.IAuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    private final ProfileClient profileClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (UserException | JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            User user = userRepository
                    .findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserException("Not found user"));

            boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

            if (!authenticated) throw new UserException("Not found user");

            var token = generateToken(user);

            return AuthenticationResponse.builder().token(token).build();
        } catch (UserException | RuntimeException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    @Override
    public ProfileResponse registration(RegistrationRequest request) {
        try {
            HashSet<Role> roles = new HashSet<>();

            roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

            User user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(roles)
                    .build();

            user = userRepository.save(user);

            ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();

            return profileClient.createProfile(profileCreationRequest).getData();
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserId().toString())
                .issuer("tronglam")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException, UserException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new UserException("Not found user");

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private record TokenInfo(String token, Date expiryDate) {
    }
}
