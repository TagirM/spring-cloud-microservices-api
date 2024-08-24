package com.javastart.depositservice.service;

import com.javastart.deposit.controller.dto.DepositResponseDTO;
import com.javastart.deposit.exception.DepositServiceException;
import com.javastart.deposit.repository.DepositRepository;
import com.javastart.deposit.rest.AccountServiceClient;
import com.javastart.deposit.rest.BillServiceClient;
import com.javastart.deposit.service.DepositService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import static com.javastart.depositservice.util.DepositUtil.createAccountResponseDTO;
import static com.javastart.depositservice.util.DepositUtil.createBillResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class DepositServiceTest {

    @Mock
    private DepositRepository depositRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private BillServiceClient billServiceClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private DepositService depositService;

    @Test
    public void depositServiceTest_withBillId(){
        Mockito.when(billServiceClient.getBillById(ArgumentMatchers.anyLong()))
                .thenReturn(createBillResponseDTO());
        Mockito.when(accountServiceClient.getAccountByID(ArgumentMatchers.anyLong()))
                .thenReturn(createAccountResponseDTO());
        DepositResponseDTO deposit = depositService.deposit(null, 1L, BigDecimal.valueOf(1000));
        Assertions.assertThat(deposit.getMail()).isEqualTo("lori@cat.xyz");
    }

    @Test(expected = DepositServiceException.class)
    public void depositServiceTest_exception(){
        depositService.deposit(null, null, BigDecimal.valueOf(1000));
    }

}
