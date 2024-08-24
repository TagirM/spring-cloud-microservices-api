package com.javastart.depositservice.util;

import com.javastart.deposit.rest.AccountResponseDTO;
import com.javastart.deposit.rest.BillResponseDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;

public class DepositUtil {
    public static BillResponseDTO createBillResponseDTO(){
        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setBillId(1L);
        billResponseDTO.setAccountId(1L);
        billResponseDTO.setAmount(BigDecimal.valueOf(1000));
        billResponseDTO.setCreationDate(OffsetDateTime.now());
        billResponseDTO.setIsDefault(true);
        billResponseDTO.setOverDraftEnable(true);
        return billResponseDTO;
    }

    public static AccountResponseDTO createAccountResponseDTO(){
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountId(1L);
        accountResponseDTO.setCreationDate(OffsetDateTime.now());
        accountResponseDTO.setBills(Arrays.asList(1L,2L,3L));
        accountResponseDTO.setEmail("lori@cat.xyz");
        accountResponseDTO.setName("Lori");
        accountResponseDTO.setPhone("+79823876917");
        return accountResponseDTO;
    }
}
