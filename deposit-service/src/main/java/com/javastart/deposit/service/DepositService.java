package com.javastart.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.deposit.controller.dto.DepositResponseDTO;
import com.javastart.deposit.entity.Deposit;
import com.javastart.deposit.exception.DepositServiceException;
import com.javastart.deposit.repository.DepositRepository;
import com.javastart.deposit.rest.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class DepositService {

    private static final String TOPIC_EXCHANGE_DEPOSIT = "js.deposit.notify.exchange";
    private static final String ROUTING_KEY_DEPOSIT = "js.key.deposit";
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private AccountServiceClient accountServiceClient;
    @Autowired
    private BillServiceClient billServiceClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public DepositResponseDTO deposit(Long accountId, Long billId, BigDecimal amount){
        if (accountId == null && billId == null){
            throw new DepositServiceException("Id of account is null and id of bill is null");
        }

        if (billId!=0){
            BillResponseDTO billResponseDTO = billServiceClient.getBillById(billId);
            BillRequestDTO billRequestDTO = createBillRequestDTO(amount, billResponseDTO);

            billServiceClient.update(billId, billRequestDTO);
            AccountResponseDTO accountResponseDTO = accountServiceClient.getAccountByID(billResponseDTO.getAccountId());
            depositRepository.save(new Deposit(amount, billId, OffsetDateTime.now(), accountResponseDTO.getEmail()));

            return createResponse(amount, accountResponseDTO);
        }
        BillResponseDTO defaultBill = getDefaultBill(accountId);
        BillRequestDTO billRequestDTO = createBillRequestDTO(amount,defaultBill);
        billServiceClient.update(defaultBill.getBillId(), billRequestDTO);
        AccountResponseDTO account = accountServiceClient.getAccountByID(accountId);
        depositRepository.save(new Deposit(amount, defaultBill.getBillId(), OffsetDateTime.now(), account.getEmail()));
        return createResponse(amount, account);
    }

    private DepositResponseDTO createResponse(BigDecimal amount, AccountResponseDTO accountResponseDTO) {
        DepositResponseDTO depositResponseDTO = new DepositResponseDTO(amount, accountResponseDTO.getEmail());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_DEPOSIT, ROUTING_KEY_DEPOSIT, objectMapper
                    .writeValueAsString(depositResponseDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DepositServiceException("Can not send message to RabbitMQ");
        }
        return depositResponseDTO;
    }

    private BillRequestDTO createBillRequestDTO(BigDecimal amount, BillResponseDTO billResponseDTO) {
        BillRequestDTO billRequestDTO = new BillRequestDTO();
        billRequestDTO.setAccountId(billResponseDTO.getAccountId());
        billRequestDTO.setAmount(billResponseDTO.getAmount().add(amount));
        billRequestDTO.setCreationDate(billResponseDTO.getCreationDate());
        billRequestDTO.setIsDefault(billResponseDTO.getIsDefault());
        billRequestDTO.setOverDraftEnable(billResponseDTO.getOverDraftEnable());
        return billRequestDTO;
    }

    private BillResponseDTO getDefaultBill(Long accountId){
        return billServiceClient.getBillsByAccountId(accountId).stream().
                filter(BillResponseDTO::getIsDefault).findAny()
                .orElseThrow(()->new DepositServiceException("Unable to find default bill for account: " + accountId));
    }
}
