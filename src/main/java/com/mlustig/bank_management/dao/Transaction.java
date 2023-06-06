package com.mlustig.bank_management.dao;

import com.mlustig.bank_management.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Builder
@Accessors(chain = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_id_sequence",
            sequenceName = "transaction_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_id_sequence"
    )
    @EqualsAndHashCode.Exclude
    private Long id;

    @NonNull
    @Column(nullable = false)
    private Long bankAccountId;

    @NonNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(Clock.systemDefaultZone());
}
