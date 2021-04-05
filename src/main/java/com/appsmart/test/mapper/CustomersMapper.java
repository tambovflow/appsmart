package com.appsmart.test.mapper;

import com.appsmart.test.domain.CustomerEntity;
import com.appsmart.test.dto.CustomerDto;
import com.appsmart.test.models.requests.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomersMapper {

    CustomerEntity customerRequestToEntity(CustomerRequest request);

    CustomerDto customerEntityToDto(CustomerEntity customerEntity);

    void updateCustomerFromRequest(CustomerRequest request, @MappingTarget CustomerEntity customerEntity);
}
