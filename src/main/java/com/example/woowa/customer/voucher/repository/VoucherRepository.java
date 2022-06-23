package com.example.woowa.customer.voucher.repository;

import com.example.woowa.customer.voucher.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

}
