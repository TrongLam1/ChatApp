package com.chat.app.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.chat.app.model.Account;

public interface IAccountService {

	UserDetailsService userDetailsService();
	Optional<Account> findByEmail(String email);
}
