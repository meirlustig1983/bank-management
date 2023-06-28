package com.mlustig.bank_management.mappers;

import com.mlustig.bank_management.dao.BankAccountStatus;
import com.mlustig.bank_management.dto.BankAccountStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountStatusMapper {
    BankAccountStatusMapper INSTANCE = Mappers.getMapper(BankAccountStatusMapper.class);

    BankAccountStatusDto toDto(BankAccountStatus bankAccountStatus);

    @Mapping(target = "id", ignore = true)
    BankAccountStatus toDao(BankAccountStatusDto bankAccountStatusDto);
}