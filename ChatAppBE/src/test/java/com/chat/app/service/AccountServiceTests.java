package com.chat.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.chat.app.model.Account;
import com.chat.app.model.User;
import com.chat.app.model.enums.Role;
import com.chat.app.repository.AccountRepository;
import com.chat.app.service.impl.AccountServiceImpl;

@SpringBootTest
public class AccountServiceTests {

	@Autowired
	private AccountServiceImpl accountService;
	
	@MockBean
	private AccountRepository accountRepo;
	
	private User user;
	private Account account;
	private final String existingEmail = "existinguser@example.com";
    private final String nonExistingEmail = "nonexistinguser@example.com";
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		user = User.builder()
				.userId(1).email(existingEmail).build();
		account = Account.builder()
				.accountId("account01").email(existingEmail)
				.role(Role.USER).user(user)
				.build();
	}
	
	@Test
    void loadUserByUsername_userExists() {
        when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.of(account));

        UserDetailsService userDetailsService = accountService.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(existingEmail);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(existingEmail);
    }

    @Test
    void loadUserByUsername_userDoesNotExist() {
        when(accountRepo.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = accountService.userDetailsService();

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(nonExistingEmail))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("User not founded!");
    }
	
	@Test
	void testFindByEmail_success() {
		when(accountRepo.findByEmail(existingEmail)).thenReturn(Optional.of(account));
		
		var response = accountService.findByEmail(existingEmail);
		assertThat(response.get().getUser().equals(user));
	}
}
