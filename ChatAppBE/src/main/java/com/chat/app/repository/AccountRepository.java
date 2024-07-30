package com.chat.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.app.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

	Optional<Account> findByEmail(String email);
	
	Optional<Account> findByRefreshToken(String token);
}
