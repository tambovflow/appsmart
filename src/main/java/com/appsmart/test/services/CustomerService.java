package com.appsmart.test.services;

import com.appsmart.test.domain.CustomerEntity;
import com.appsmart.test.dto.CustomerDto;
import com.appsmart.test.mapper.CustomersMapper;
import com.appsmart.test.models.requests.CustomerRequest;
import com.appsmart.test.repositories.CustomersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This service for customers CRUD
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomersMapper customersMapper;
    private final CustomersRepository customersRepository;

    public CustomerDto createCustomer(CustomerRequest customerRequest) {
        log.info("create new customer");
        CustomerEntity customerEntity = customersMapper.customerRequestToEntity(customerRequest);
        customersRepository.save(customerEntity);
        return customersMapper.customerEntityToDto(customerEntity);
    }

    public Page<CustomerDto> getCustomers(Integer page, Integer size) {

        log.info("get customers");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CustomerEntity> customerEntities = customersRepository.getCustomersPage(pageable);
        return customerEntities.map(customersMapper::customerEntityToDto);
    }

    public CustomerDto getCustomerById(UUID customerId) {

        log.info("get customer by id");
        CustomerEntity customerEntity = customersRepository.getCustomerEntityById(customerId);
        return customersMapper.customerEntityToDto(customerEntity);
    }

    @Transactional
    public CustomerDto updateCustomerById(UUID customerId, CustomerRequest customerRequest) {

        log.info("update customer by id");
        CustomerEntity customerEntity = customersRepository.getCustomerEntityById(customerId);
        customersMapper.updateCustomerFromRequest(customerRequest, customerEntity);

        customerEntity.setModifiedAt(LocalDateTime.now());
        return customersMapper.customerEntityToDto(customerEntity);
    }

    public void deleteCustomerById(UUID customerId) {

        log.info("delete customer by id");
        CustomerEntity customerEntity = customersRepository.getCustomerEntityById(customerId);
        customerEntity.setModifiedAt(LocalDateTime.now());
        customerEntity.setIsDeleted(true);

        customersRepository.save(customerEntity);
    }
}
