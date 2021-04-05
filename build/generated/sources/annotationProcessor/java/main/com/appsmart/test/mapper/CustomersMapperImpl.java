package com.appsmart.test.mapper;

import com.appsmart.test.domain.CustomerEntity;
import com.appsmart.test.dto.CustomerDto;
import com.appsmart.test.models.requests.CustomerRequest;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-05T16:36:26+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.3.jar, environment: Java 1.8.0_212 (Oracle Corporation)"
)
@Component
public class CustomersMapperImpl implements CustomersMapper {

    @Override
    public CustomerEntity customerRequestToEntity(CustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setTitle( request.getTitle() );

        return customerEntity;
    }

    @Override
    public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
        if ( customerEntity == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        customerDto.setId( customerEntity.getId() );
        customerDto.setTitle( customerEntity.getTitle() );
        customerDto.setIsDeleted( customerEntity.getIsDeleted() );
        customerDto.setCreatedAt( customerEntity.getCreatedAt() );
        customerDto.setModifiedAt( customerEntity.getModifiedAt() );

        return customerDto;
    }

    @Override
    public void updateCustomerFromRequest(CustomerRequest request, CustomerEntity customerEntity) {
        if ( request == null ) {
            return;
        }

        customerEntity.setTitle( request.getTitle() );
    }
}
