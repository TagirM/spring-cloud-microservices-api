package com.javastart.deposit.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class DepositRequestDTO {

    private Long accountId;

    private Long billId;

    private BigDecimal amount;
}
