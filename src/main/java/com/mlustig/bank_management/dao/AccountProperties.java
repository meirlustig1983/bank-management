package com.mlustig.bank_management.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "account_properties")
public class AccountProperties {

    @Id
    @SequenceGenerator(
            name = "account_properties_id_sequence",
            sequenceName = "account_properties_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_info_id_sequence"
    )
    @EqualsAndHashCode.Exclude
    private Long accountPropertiesId;

    private BigDecimal creditLimit;

    private boolean active;

    @OneToOne
    @JoinColumn(name = "accountInfoId", nullable = false, unique = true)
    private AccountInfo accountInfo;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(Clock.systemDefaultZone());

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(Clock.systemDefaultZone());
}