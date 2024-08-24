package com.javastart.bill.service;

import com.javastart.bill.entity.Bill;
import com.javastart.bill.exception.BillNotFoundException;
import com.javastart.bill.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new BillNotFoundException(
                "Unable to find account with id: " + billId));
    }

    public Long createBill(Long accountId, BigDecimal amount, Boolean isDefault, Boolean overDraftEnable) {
        Bill bill = new Bill(accountId, amount, isDefault, OffsetDateTime.now(), overDraftEnable);
        return billRepository.save(bill).getBillId();
    }

    public Bill updateBill(Long billId, Long accountId, BigDecimal amount, Boolean isDefault, Boolean overDraftEnable) {
        Bill bill = new Bill();
        bill.setBillId(billId);
        bill.setAccountId(accountId);
        bill.setAmount(amount);
        bill.setIsDefault(isDefault);
        bill.setOverDraftEnable(overDraftEnable);
        return billRepository.save(bill);
    }

    public Bill deleteBill(Long billId) {
        Bill deleteBillById = getBillById(billId);
        billRepository.deleteById(billId);
        return deleteBillById;
    }

    public List<Bill> getBillsByAccountId(Long accountId){
        return billRepository.getBillsByAccountId(accountId);
    }
}
