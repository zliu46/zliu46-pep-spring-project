package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.exception.InvalidAccountDataException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new InvalidAccountDataException("Username is invalid");
        } 

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new InvalidAccountDataException("Password must be at least 4 characters");
        }
        
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username " + account.getUsername() + " is already taken");
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        return null;
    }
}
