package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chat.app.model.Account;
import com.chat.app.model.User;
import com.chat.app.model.enums.Role;
import com.chat.app.repository.AccountRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.request.AuthenRequest;
import com.chat.app.request.SignUpRequest;
import com.chat.app.service.impl.AuthenticationServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

@SpringBootTest
public class AuthenticationServiceTests {

	@Autowired
	private AuthenticationServiceImpl authenticationService;
	
	@MockBean
	private AccountRepository accountRepo;

	@MockBean
	private UserRepository userRepo;

	@MockBean
	private JwtServiceImpl jwtService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private PasswordEncoder passwordEncoder;
	
	private Account account;
	private User user;
	private SignUpRequest signUpReq;
	private AuthenRequest authenReq;
	private final String existingEmail = "existinguser@example.com";
    private final String nonExistingEmail = "nonexistinguser@example.com";
    private final String token = "validToken";
    private final String refreshToken = "validToken";
	
	@BeforeEach
	void init() {
		signUpReq = SignUpRequest.builder()
				.username("test")
				.email(existingEmail)
				.password("12345678").build();
		authenReq = AuthenRequest.builder()
				.email(existingEmail)
				.password("12345678").build();
		user = User.builder()
				.userId(1).email(existingEmail).userName("test").build();
		account = Account.builder()
				.accountId("account01").email(existingEmail)
				.role(Role.USER).user(user)
				.build();
	}
	
//	Function signUp
	@Test
	void testSignUp_success() {
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.empty());
		
		var res = authenticationService.signUp(signUpReq);
		assertThat(res.equals("Successful account registration."));
	}
	
	@Test
	void testSignUp_emailHasBeenUsed_failed() {
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.of(account));
		
		assertThatThrownBy(() -> authenticationService.signUp(signUpReq))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("java.lang.RuntimeException: Email has been used.");
	}
	
//	Function signIn
	@Test
	void testSignIn_success() {
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.of(account));
		
		var res = authenticationService.signIn(authenReq);
		assertThat(res.getName().equals(user.getUserName()));
	}
	
	@Test
	void testSignIn_notFoundAccount_failed() {
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> authenticationService.signIn(authenReq))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error: java.lang.RuntimeException: Not found account.");
	}
	
//	Function refreshToken
	@Test
	void testRefreshToken_success() {
		when(accountRepo.findByRefreshToken(refreshToken)).thenReturn(Optional.of(account));
		when(jwtService.generateToken(account)).thenReturn(token);
		when(jwtService.generateRefreshToken(new HashMap<>(), account)).thenReturn(refreshToken);
		
		var res = authenticationService.refreshToken(token);
		assertThat(res.getToken()).isNotNull();
		assertThat(res.getRefreshToken()).isNotNull();
	}
	
	@Test
	void testRefreshToken_notFoundAccount_failed() {
		when(accountRepo.findByRefreshToken(refreshToken)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> authenticationService.refreshToken(token))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error: java.lang.RuntimeException: Invalid refresh token.");
	}
	
//	Function logOut
	@Test
	void testLogOut_success() {
		when(jwtService.extractUsername(token)).thenReturn(existingEmail);
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.of(account));
		
		var res = authenticationService.logOut(token);
		assertThat(res.equals("Log out success."));
		assertThat(account.getRefreshToken()).isNull();
	}
	
	@Test
	void testLogOut_notFoundAccount_failed() {
		when(jwtService.extractUsername(token)).thenReturn(existingEmail);
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> authenticationService.logOut(token))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("java.lang.RuntimeException: User information does not exist.");
	}
}
