package com.chat.app.service.impl;

import com.chat.app.model.Account;
import com.chat.app.repository.AccountRepository;
import com.chat.app.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepo;

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
        return accountRepo.findByEmail(email);
    }
}
