package com.mlustig.bank_management.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Accessors(chain = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "bank_account", indexes = {@Index(name = "bank_account_account_id_idx", columnList = "accountId")})
public class BankAccountStatus {
    @Id
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String accountId;

    private boolean active;
}
