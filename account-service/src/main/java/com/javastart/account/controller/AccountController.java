package com.javastart.account.controller;

import com.javastart.account.controller.dto.AccountResponseDTO;
import com.javastart.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountId}")
    public AccountResponseDTO getAccount(@PathVariable Long accountId) {
        return new AccountResponseDTO(accountService.getAccountById(accountId));
    }

    @PostMapping("/")
    public Long createAccountResponseDTO(@RequestBody AccountResponseDTO accountResponseDTO){
        return accountService.createAccount(accountResponseDTO.getName(), accountResponseDTO.getEmail(),
                accountResponseDTO.getPhone(), accountResponseDTO.getBills());
    }

    @PutMapping("/{accountId}")
    public AccountResponseDTO updateAccountResponseDTO(@RequestBody AccountResponseDTO accountResponseDTO,
                                                       @PathVariable Long accountId){
        return new AccountResponseDTO(accountService.updateAccount(accountId, accountResponseDTO.getName(), accountResponseDTO.getEmail(),
                accountResponseDTO.getPhone(), accountResponseDTO.getBills()));
    }

    @DeleteMapping("/{accountId}")
    public AccountResponseDTO deleteAccountResponseDTO(@PathVariable Long accountId){
        return new AccountResponseDTO(accountService.deleteAccount(accountId));
    }
}
