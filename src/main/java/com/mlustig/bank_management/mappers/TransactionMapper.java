package com.mlustig.bank_management.mappers;

import com.mlustig.bank_management.dao.Transaction;
import com.mlustig.bank_management.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto toDto(Transaction transaction);
}