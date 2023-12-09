package com.javastart.bill.controller;

import com.javastart.bill.controller.dto.BillResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.javastart.bill.service.BillService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/{billId}")
    public BillResponseDTO getBill(@PathVariable Long billId) {
        return new BillResponseDTO(billService.getBillById(billId));
    }

    @PostMapping("/")
    public Long createBillResponseDTO(@RequestBody BillResponseDTO billResponseDTO){
        return billService.createBill(billResponseDTO.getAccountId(), billResponseDTO.getAmount(),
                billResponseDTO.getIsDefault(), billResponseDTO.getOverDraftEnable());
    }

    @PutMapping("/{billId}")
    public BillResponseDTO updateBillResponseDTO(@RequestBody BillResponseDTO billResponseDTO,
                                                       @PathVariable Long billId){
        return new BillResponseDTO(billService.updateBill(billId, billResponseDTO.getAccountId(), billResponseDTO.getAmount(),
                billResponseDTO.getIsDefault(), billResponseDTO.getOverDraftEnable()));
    }

    @DeleteMapping("/{billId}")
    public BillResponseDTO deleteBillResponseDTO(@PathVariable Long billId){
        return new BillResponseDTO(billService.deleteBill(billId));
    }

    @GetMapping("/account/{accountId}")
    public List<BillResponseDTO> getBillsByAccountId(@PathVariable Long accountId){
        return billService.getBillsByAccountId(accountId).stream().map(BillResponseDTO::new).collect(Collectors.toList());
    }
}
