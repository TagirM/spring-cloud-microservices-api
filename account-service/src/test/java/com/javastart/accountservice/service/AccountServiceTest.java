package com.javastart.accountservice.service;

import com.javastart.account.entity.Account;
import com.javastart.account.exception.AccountNotFoundException;
import com.javastart.account.repository.AccountRepository;
import com.javastart.account.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.OffsetDateTime;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void AccountServiceTest_withAccountId(){
        Mockito.when(accountService.getAccountById(1L)).thenReturn(createAccount());
        Account account = accountService.getAccountById(1L);
        Assertions.assertThat(account.getAccountId()).isEqualTo(1L);
    }

    @Test(expected = AccountNotFoundException.class)
    public void AccountServiceTest_exception(){
        accountService.getAccountById(null);
    }


    private Account createAccount() {
        Account account = new Account(
                "Lori", "lori@cat.xyz", "+79823876917", OffsetDateTime.now(), Arrays.asList(1L,2L,3L));
        account.setAccountId(1L);
        return account;
    }


}
