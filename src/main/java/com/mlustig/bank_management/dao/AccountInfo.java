package com.mlustig.bank_management.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Accessors(chain = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "account_info",
        indexes = {@Index(name = "account_info_user_name_idx", columnList = "userName"),
                @Index(name = "account_info_email_idx", columnList = "email"),
                @Index(name = "account_info_phone_number_idx", columnList = "phoneNumber")}
)
public class AccountInfo {

    @Id
    @SequenceGenerator(
            name = "account_info_id_sequence",
            sequenceName = "account_info_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_info_id_sequence"
    )
    @EqualsAndHashCode.Exclude
    private Long accountInfoId;

    @NonNull
    @Column(nullable = false, unique = true)
    private String userName;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @OneToOne(mappedBy = "accountInfo", fetch = FetchType.LAZY)
    private AccountBalance accountBalance;

    @OneToOne(mappedBy = "accountInfo", fetch = FetchType.LAZY)
    private AccountProperties accountProperties;

    @OneToMany(mappedBy = "accountInfo", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(Clock.systemDefaultZone());

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(Clock.systemDefaultZone());
}