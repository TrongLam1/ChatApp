package com.chat.app.service.impl;

import com.chat.app.exception.UserException;
import com.chat.app.model.Account;
import com.chat.app.model.User;
import com.chat.app.model.enums.Role;
import com.chat.app.repository.AccountRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.AuthenRequest;
import com.chat.app.request.SignUpRequest;
import com.chat.app.response.JwtAuthenticationResponse;
import com.chat.app.service.IAuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final AccountRepository accountRepo;

    private final UserRepository userRepo;

    private final JwtServiceImpl jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private String generateCustomId() {
        long count = accountRepo.count();
        return count < 10 ? "account0" + count : "account" + count;
    }

    @Override
    public String signUp(SignUpRequest request) {
        try {
            boolean isCheckedEmail = accountRepo.findByEmail(request.getEmail()).isPresent();

            if (!isCheckedEmail) {
                Account account = new Account();
                account.setAccountId(generateCustomId());
                account.setEmail(request.getEmail());
                account.setPassword(passwordEncoder.encode(request.getPassword()));

                User user = new User();
                user.setEmail(request.getEmail());
                user.setUserName(request.getUsername());

                userRepo.save(user);

                account.setUser(user);
                account.setRole(Role.USER);

                accountRepo.save(account);

                return "Successful account registration.";
            } else {
                throw new UserException("Email has been used.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public JwtAuthenticationResponse signIn(AuthenRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Account account = accountRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Not found account."));

            var jwtToken = jwtService.generateToken(account);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), account);

            JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
            jwtResponse.setToken(jwtToken);
            jwtResponse.setRefreshToken(refreshToken);
            jwtResponse.setName(account.getUser().getUserName());
            jwtResponse.setExpiredTime(jwtService.isTokenExpiredTime(jwtToken));
            jwtResponse.setRole(account.getRole());
            jwtResponse.setAvatar(account.getUser().getImage_url());
            jwtResponse.setUserId(account.getUser().getUserId());
            account.setRefreshToken(refreshToken);

            accountRepo.save(account);

            return jwtResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.toString());
        }
    }

    @Override
    public JwtAuthenticationResponse refreshToken(String token) {
        try {
            Account account = accountRepo.findByRefreshToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid refresh token."));

            var jwtToken = jwtService.generateToken(account);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), account);

            JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
            jwtResponse.setToken(jwtToken);
            jwtResponse.setRefreshToken(refreshToken);
            jwtResponse.setName(account.getEmail());
            jwtResponse.setExpiredTime(jwtService.isTokenExpiredTime(jwtToken));
            account.setRefreshToken(refreshToken);

            accountRepo.save(account);

            return jwtResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.toString());
        }
    }

    @Override
    public String logOut(String token) {
        try {
            String email = jwtService.extractUsername(token);
            Account account = accountRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User information does not exist."));
            account.setRefreshToken(null);

            return "Log out success.";
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
