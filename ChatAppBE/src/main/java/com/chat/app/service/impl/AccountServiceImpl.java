package com.chat.app.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chat.app.model.Account;
import com.chat.app.repository.AccountRepository;
import com.chat.app.service.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountRepository accountRepo;
	
	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return accountRepo.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not founded!"));
			}
		};
	}

	@Override
	public Optional<Account> findByEmail(String email) {
		try {
			return accountRepo.findByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
	}
}
