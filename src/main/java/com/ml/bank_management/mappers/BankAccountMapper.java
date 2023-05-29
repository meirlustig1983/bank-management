package com.ml.bank_management.mappers;

import com.ml.bank_management.dao.BankAccount;
import com.ml.bank_management.dao.Transaction;
import com.ml.bank_management.dto.BankAccountDto;
import com.ml.bank_management.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountDto toDto(BankAccount bankAccount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    BankAccount toDao(BankAccountDto bankAccountDto);

    default List<TransactionDto> mapTransactions(List<Transaction> transactions) {
        return Optional.ofNullable(transactions)
                .map(transactionsList -> transactionsList.stream()
                        .map(TransactionMapper.INSTANCE::toDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}