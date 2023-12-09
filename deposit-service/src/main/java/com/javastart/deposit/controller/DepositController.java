package com.javastart.deposit.controller;

import com.javastart.deposit.controller.dto.DepositRequestDTO;
import com.javastart.deposit.controller.dto.DepositResponseDTO;
import com.javastart.deposit.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @PostMapping("/deposits")
    public DepositResponseDTO getDeposit(@RequestBody DepositRequestDTO depositRequestDTO) {
        return depositService.deposit(depositRequestDTO.getAccountId(), depositRequestDTO.getBillId()
                ,depositRequestDTO.getAmount());
    }
}
