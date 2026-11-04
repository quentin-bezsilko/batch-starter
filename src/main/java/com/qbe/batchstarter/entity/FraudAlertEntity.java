package com.qbe.batchstarter.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "FRAUD_ALERT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fraudAlertSeq")
    @SequenceGenerator(name = "fraudAlertSeq", sequenceName = "FRAUD_ALERT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "TRANSACTION_ID", nullable = false)
    private Long transactionId;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;

    @Column(name = "FRAUD_TYPE", nullable = false)
    private String fraudType;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "REASON", nullable = false)
    private String reason;

    @Column(name = "DETECTION_DATE", nullable = false)
    private LocalDateTime detectionDate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
}
