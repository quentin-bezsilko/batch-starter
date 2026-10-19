package com.qbe.batchstarter.entity;

import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "BANK_ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bankAccountSeq")
    @SequenceGenerator(name = "bankAccountSeq", sequenceName = "BANK_ACCOUNT_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customer;

    @Column(name = "IBAN", nullable = false, length = 34)
    private String iban;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private AccountType accountType;

    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_STATUS", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "OPENING_DATE", nullable = false)
    private LocalDate openingDate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
