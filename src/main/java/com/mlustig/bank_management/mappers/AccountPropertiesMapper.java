package com.mlustig.bank_management.mappers;

import com.mlustig.bank_management.dao.AccountProperties;
import com.mlustig.bank_management.dto.AccountPropertiesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountPropertiesMapper {
    AccountPropertiesMapper INSTANCE = Mappers.getMapper(AccountPropertiesMapper.class);

    AccountPropertiesDto toDto(AccountProperties accountProperties);

    @Mapping(target = "accountPropertiesId", ignore = true)
    @Mapping(target = "accountInfo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AccountProperties toDao(AccountPropertiesDto accountPropertiesDto);
}